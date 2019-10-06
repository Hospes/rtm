package ua.hospes.rtm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import ua.hospes.rtm.db.AppDatabase
import ua.hospes.rtm.db.cars.CarDAO
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.race.RaceDAO
import ua.hospes.rtm.db.sessions.SessionDAO
import ua.hospes.rtm.db.team.TeamDAO
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.race.models.toEntity

internal class RaceRepositoryImpl(db: AppDatabase) : RaceRepository {
    private val dao: RaceDAO = db.raceDao()
    private val teamDAO: TeamDAO = db.teamDao()
    private val driverDAO: DriverDAO = db.driverDao()
    private val carDAO: CarDAO = db.carDao()
    private val sessionDAO: SessionDAO = db.sessionDao()


    override suspend fun get(): List<RaceItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listen(): Flow<List<RaceItem>> = dao.observe()
            .map { list -> list.map { it.toDomain(teamDAO, driverDAO, carDAO, sessionDAO) } }

    override fun listen(id: Long): Flow<RaceItem> = dao.observe(id)
            .map { it.toDomain(teamDAO, driverDAO, carDAO, sessionDAO) }

    override suspend fun save(race: RaceItem) = withContext(Dispatchers.IO) {
        Timber.d("Save: $race | Entity: ${race.toEntity()}")
        dao.save(race.toEntity()).let { Unit }
    }

    override suspend fun save(vararg races: RaceItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun reset() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(item: RaceItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        dao.clear()
        sessionDAO.clear()
    }

    //    override fun get(): Observable<RaceItem> {
    //        return raceDbStorage.get()
    //                .flatMapSingle { raceItemDb ->
    //                    Single.zip(
    //                            Single.just(raceItemDb),
    //                            getTeamById(raceItemDb.teamId),
    //                            getSessionById(raceItemDb.sessionId),
    //                            Function3 { db: RaceEntity, team: Optional<Team>, session: Optional<Session> -> RaceMapper.map(db, team, session) })
    //                }
    //                .flatMapSingle { raceItem -> Single.zip(Single.just(raceItem), getRawSessionsByTeamId(raceItem.team.id!!), CalculateRaceItemDetails()) }
    //    }
    //
    //    override fun listen(): Observable<List<RaceItem>> {
    //        return raceDbStorage.listen()
    //                .flatMap { raceItemDbs ->
    //                    Observable.fromIterable<RaceEntity>(raceItemDbs)
    //                            .flatMapSingle { raceItemDb -> Single.zip<RaceEntity, Optional<Team>, Optional<Session>, RaceItem>(Single.just<RaceEntity>(raceItemDb), getTeamById(raceItemDb.teamId), getSessionById(raceItemDb.sessionId), Function3<RaceEntity, Optional<Team>, Optional<Session>, RaceItem> { db, team, session -> RaceMapper.map(db, team, session) }) }
    //                            .flatMapSingle { raceItem -> Single.zip(Single.just(raceItem), getRawSessionsByTeamId(raceItem.team.id!!), CalculateRaceItemDetails()) }
    //                            .toList().toObservable()
    //                }
    //    }
    //
    //    override fun listen(id: Int): Observable<RaceItem> {
    //        return raceDbStorage.listen(id)
    //                .flatMap { raceItemDbs ->
    //                    Observable.fromIterable<RaceEntity>(raceItemDbs)
    //                            .flatMapSingle { raceItemDb -> Single.zip<RaceEntity, Optional<Team>, Optional<Session>, RaceItem>(Single.just<RaceEntity>(raceItemDb), getTeamById(raceItemDb.teamId), getSessionById(raceItemDb.sessionId), Function3<RaceEntity, Optional<Team>, Optional<Session>, RaceItem> { db, team, session -> RaceMapper.map(db, team, session) }) }
    //                            .flatMapSingle { raceItem -> Single.zip(Single.just(raceItem), getRawSessionsByTeamId(raceItem.team.id!!), CalculateRaceItemDetails()) }
    //                            .toList().toObservable()
    //                }
    //                .flatMapIterable { raceItems -> raceItems }
    //    }
    //
    //    override fun addNew(vararg items: RaceItem): Observable<Boolean> {
    //        return Observable.fromArray(*items)
    //                .map { RaceMapper.map(it) }
    //                .toList()
    //                .flatMapObservable { raceDbStorage.add(it) }
    //                .map { result -> result.getResult() > 0 }
    //    }
    //
    //    override fun update(items: List<RaceItem>): Observable<Boolean> {
    //        return Observable.fromIterable(items)
    //                .map { RaceMapper.map(it) }
    //                .map { UpdateRaceOperation(it) }
    //                .toList()
    //                .flatMapObservable { raceDbStorage.updateRaces(it) }
    //    }
    //
    //    override fun updateByTeamId(items: Iterable<Pair<Int, ContentValues>>): Observable<Boolean> {
    //        return Observable.fromIterable(items)
    //                .map { UpdateRaceOperation(it) }
    //                .toList()
    //                .flatMapObservable { raceDbStorage.updateRaces(it) }
    //    }
    //
    //    override fun remove(item: RaceItem): Single<Int> {
    //        return raceDbStorage.remove(RaceMapper.map(item))
    //    }
    //
    //
    //    override fun reset(): Observable<Void> {
    //        return raceDbStorage.reset()
    //    }
    //
    //    override fun removeAll(): Single<Int> {
    //        return raceDbStorage.removeAll()
    //    }
    //
    //
    //    private fun getTeamById(id: Int): Single<Optional<Team>> {
    //        return Single.error(RuntimeException()) //teamsRepository.get(id).map { Optional.of(it) }
    //    }
    //
    //    private fun getSessionById(id: Int): Single<Optional<Session>> {
    //        return sessionsRepository.get(id).map<Optional<Session>> { Optional.of(it) }.single(Optional.empty())
    //    }
    //
    //    private fun getRawSessionsByTeamId(teamId: Int): Single<List<SessionEntity>> {
    //        return sessionsDbStorage.getByTeamId(teamId).toList()
    //    }
    //
    //    private inner class CalculateRaceItemDetails : BiFunction<RaceItem, List<SessionEntity>, RaceItem> {
    //        @Throws(Exception::class)
    //        override fun apply(item: RaceItem, sessions: List<SessionEntity>): RaceItem {
    //            val details = RaceItemDetails()
    //            var pitStops = -1
    //            for (session in sessions) {
    //                pitStops += if (Session.Type.TRACK.name == session.type) 1 else 0
    //                if (session.startDurationTime == -1L || session.endDurationTime == -1L) continue
    //                if (session.driverId == -1) continue
    //                val duration = session.endDurationTime - session.startDurationTime
    //                details.addDriverDuration(session.driverId, duration)
    //            }
    //            details.pitStops = pitStops
    //            item.details = details
    //            return item
    //        }
    //    }
}