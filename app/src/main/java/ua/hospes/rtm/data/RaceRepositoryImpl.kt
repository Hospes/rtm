package ua.hospes.rtm.data

import android.content.ContentValues
import android.util.Pair
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import ua.hospes.rtm.db.race.RaceMapper
import ua.hospes.rtm.db.race.RaceItemDb
import ua.hospes.rtm.db.race.operations.UpdateRaceOperation
import ua.hospes.rtm.db.race.RaceDbStorage
import ua.hospes.rtm.db.sessions.SessionDb
import ua.hospes.rtm.db.sessions.SessionsDbStorage
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.race.models.RaceItemDetails
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.TeamsRepository
import ua.hospes.rtm.utils.Optional
import javax.inject.Inject

internal class RaceRepositoryImpl @Inject constructor(
        private val raceDbStorage: RaceDbStorage,
        private val teamsRepository: TeamsRepository,
        private val sessionsDbStorage: SessionsDbStorage,
        private val sessionsRepository: SessionsRepositoryImpl
) : RaceRepository {

    override fun get(): Observable<RaceItem> {
        return raceDbStorage.get()
                .flatMapSingle { raceItemDb ->
                    Single.zip(
                            Single.just(raceItemDb),
                            getTeamById(raceItemDb.teamId),
                            getSessionById(raceItemDb.sessionId),
                            Function3 { db: RaceItemDb, team: Optional<Team>, session: Optional<Session> -> RaceMapper.map(db, team, session) })
                }
                .flatMapSingle { raceItem -> Single.zip(Single.just(raceItem), getRawSessionsByTeamId(raceItem.team.id!!), CalculateRaceItemDetails()) }
    }

    override fun listen(): Observable<List<RaceItem>> {
        return raceDbStorage.listen()
                .flatMap { raceItemDbs ->
                    Observable.fromIterable<RaceItemDb>(raceItemDbs)
                            .flatMapSingle { raceItemDb -> Single.zip<RaceItemDb, Optional<Team>, Optional<Session>, RaceItem>(Single.just<RaceItemDb>(raceItemDb), getTeamById(raceItemDb.teamId), getSessionById(raceItemDb.sessionId), Function3<RaceItemDb, Optional<Team>, Optional<Session>, RaceItem> { db, team, session -> RaceMapper.map(db, team, session) }) }
                            .flatMapSingle { raceItem -> Single.zip(Single.just(raceItem), getRawSessionsByTeamId(raceItem.team.id!!), CalculateRaceItemDetails()) }
                            .toList().toObservable()
                }
    }

    override fun listen(id: Int): Observable<RaceItem> {
        return raceDbStorage.listen(id)
                .flatMap { raceItemDbs ->
                    Observable.fromIterable<RaceItemDb>(raceItemDbs)
                            .flatMapSingle { raceItemDb -> Single.zip<RaceItemDb, Optional<Team>, Optional<Session>, RaceItem>(Single.just<RaceItemDb>(raceItemDb), getTeamById(raceItemDb.teamId), getSessionById(raceItemDb.sessionId), Function3<RaceItemDb, Optional<Team>, Optional<Session>, RaceItem> { db, team, session -> RaceMapper.map(db, team, session) }) }
                            .flatMapSingle { raceItem -> Single.zip(Single.just(raceItem), getRawSessionsByTeamId(raceItem.team.id!!), CalculateRaceItemDetails()) }
                            .toList().toObservable()
                }
                .flatMapIterable { raceItems -> raceItems }
    }

    override fun addNew(vararg items: RaceItem): Observable<Boolean> {
        return Observable.fromArray(*items)
                .map { RaceMapper.map(it) }
                .toList()
                .flatMapObservable { raceDbStorage.add(it) }
                .map { result -> result.getResult() > 0 }
    }

    override fun update(items: List<RaceItem>): Observable<Boolean> {
        return Observable.fromIterable(items)
                .map { RaceMapper.map(it) }
                .map { UpdateRaceOperation(it) }
                .toList()
                .flatMapObservable { raceDbStorage.updateRaces(it) }
    }

    override fun updateByTeamId(items: Iterable<Pair<Int, ContentValues>>): Observable<Boolean> {
        return Observable.fromIterable(items)
                .map { UpdateRaceOperation(it) }
                .toList()
                .flatMapObservable { raceDbStorage.updateRaces(it) }
    }

    override fun remove(item: RaceItem): Single<Int> {
        return raceDbStorage.remove(RaceMapper.map(item))
    }


    override fun reset(): Observable<Void> {
        return raceDbStorage.reset()
    }

    override fun removeAll(): Single<Int> {
        return raceDbStorage.removeAll()
    }


    private fun getTeamById(id: Int): Single<Optional<Team>> {
        return Single.error(RuntimeException()) //teamsRepository.get(id).map { Optional.of(it) }
    }

    private fun getSessionById(id: Int): Single<Optional<Session>> {
        return sessionsRepository.get(id).map<Optional<Session>> { Optional.of(it) }.single(Optional.empty())
    }

    private fun getRawSessionsByTeamId(teamId: Int): Single<List<SessionDb>> {
        return sessionsDbStorage.getByTeamId(teamId).toList()
    }

    private inner class CalculateRaceItemDetails : BiFunction<RaceItem, List<SessionDb>, RaceItem> {
        @Throws(Exception::class)
        override fun apply(item: RaceItem, sessions: List<SessionDb>): RaceItem {
            val details = RaceItemDetails()
            var pitStops = -1
            for (session in sessions) {
                pitStops += if (Session.Type.TRACK.name == session.type) 1 else 0
                if (session.startDurationTime == -1L || session.endDurationTime == -1L) continue
                if (session.driverId == -1) continue
                val duration = session.endDurationTime - session.startDurationTime
                details.addDriverDuration(session.driverId, duration)
            }
            details.pitStops = pitStops
            item.details = details
            return item
        }
    }
}