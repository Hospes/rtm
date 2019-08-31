package ua.hospes.rtm.data.race

import android.content.ContentValues
import android.util.Pair

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ua.hospes.rtm.data.race.mapper.RaceMapper
import ua.hospes.rtm.data.race.operations.UpdateRaceOperation
import ua.hospes.rtm.data.race.storage.RaceDbStorage
import ua.hospes.rtm.data.sessions.SessionsRepositoryImpl
import ua.hospes.rtm.data.sessions.models.SessionDb
import ua.hospes.rtm.data.sessions.storage.SessionsDbStorage
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.race.models.RaceItemDetails
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.sessions.SessionsRepository
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.TeamsRepository
import ua.hospes.rtm.utils.Optional

/**
 * @author Andrew Khloponin
 */
@Singleton
class RaceRepositoryImpl @Inject
internal constructor(private val raceDbStorage: RaceDbStorage, private val teamsRepository: TeamsRepository, private val sessionsDbStorage: SessionsDbStorage, sessionsRepository: SessionsRepositoryImpl) : RaceRepository {
    private val sessionsRepository: SessionsRepository


    init {
        this.sessionsRepository = sessionsRepository
    }


    override fun get(): Observable<RaceItem> {
        return raceDbStorage.get()
                .flatMapSingle { raceItemDb -> Single.zip<RaceItemDb, Optional<Team>, Optional<Session>, RaceItem>(Single.just<RaceItemDb>(raceItemDb), getTeamById(raceItemDb.teamId), getSessionById(raceItemDb.sessionId), Function3<RaceItemDb, Optional<Team>, Optional<Session>, RaceItem> { db, team, session -> RaceMapper.map(db, team, session) }) }
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
                .map<RaceItemDb>(Function<RaceItem, RaceItemDb> { RaceMapper.map(it) })
                .toList()
                .flatMapObservable<InsertResult<RaceItemDb>>(Function<List<RaceItemDb>, ObservableSource<out InsertResult<RaceItemDb>>> { raceDbStorage.add(it) })
                .map { result -> result.getResult() > 0 }
    }

    override fun update(items: List<RaceItem>): Observable<Boolean> {
        return Observable.fromIterable(items)
                .map<RaceItemDb>(Function<RaceItem, RaceItemDb> { RaceMapper.map(it) })
                .map<UpdateRaceOperation>(Function<RaceItemDb, UpdateRaceOperation> { UpdateRaceOperation(it) })
                .toList()
                .flatMapObservable(Function<List<UpdateRaceOperation>, ObservableSource<out Boolean>> { raceDbStorage.updateRaces(it) })
    }

    override fun updateByTeamId(items: Iterable<Pair<Int, ContentValues>>): Observable<Boolean> {
        return Observable.fromIterable(items)
                .map<UpdateRaceOperation>(Function<Pair<Int, ContentValues>, UpdateRaceOperation> { UpdateRaceOperation(it) })
                .toList()
                .flatMapObservable(Function<List<UpdateRaceOperation>, ObservableSource<out Boolean>> { raceDbStorage.updateRaces(it) })
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
        return teamsRepository.get(id).map(Function<Team, Optional<Team>> { Optional.of(it) })
    }

    private fun getSessionById(id: Int): Single<Optional<Session>> {
        return sessionsRepository.get(id).map<Optional<Session>>(Function<Session, Optional<Session>> { Optional.of(it) }).single(Optional.empty())
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
                if (session.startDurationTime == -1 || session.endDurationTime == -1) continue
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