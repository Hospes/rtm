package ua.hospes.rtm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.hospes.rtm.db.AppDatabase
import ua.hospes.rtm.db.cars.CarDAO
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.sessions.SessionDAO
import ua.hospes.rtm.db.sessions.SessionEntity
import ua.hospes.rtm.db.sessions.toDomain
import ua.hospes.rtm.db.team.TeamDAO
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.sessions.SessionsRepository

internal class SessionsRepositoryImpl(db: AppDatabase) : SessionsRepository {
    private val dao: SessionDAO = db.sessionDao()
    private val teamDAO: TeamDAO = db.teamDao()
    private val driverDAO: DriverDAO = db.driverDao()
    private val carDAO: CarDAO = db.carDao()
    private val raceDAO = db.raceDao()

    override suspend fun get(): List<Session> = withContext(Dispatchers.IO) {
        dao.get().map { it.toDomain(teamDAO, driverDAO, carDAO) }
    }

    override suspend fun get(vararg ids: Long): List<Session> = withContext(Dispatchers.IO) {
        dao.get(*ids).map { it.toDomain(teamDAO, driverDAO, carDAO) }
    }

    override suspend fun getByTeam(teamId: Long): List<Session> = withContext(Dispatchers.IO) {
        dao.getByTeam(teamId).map { it.toDomain(teamDAO, driverDAO, carDAO) }
    }

    override suspend fun getByTeamAndDriver(teamId: Long, driverId: Long): List<Session> = withContext(Dispatchers.IO) {
        dao.getByTeamAndDriver(teamId, driverId).map { it.toDomain(teamDAO, driverDAO, carDAO) }
    }


    override fun listen(): Flow<List<Session>> =
            dao.observe().map { list -> list.map { it.toDomain(teamDAO, driverDAO, carDAO) } }

    override fun listenByTeamId(teamId: Long): Flow<List<Session>> =
            dao.observeByTeam(teamId).map { list -> list.map { it.toDomain(teamDAO, driverDAO, carDAO) } }

    override fun listenByRaceId(raceId: Long): Flow<List<Session>> =
            dao.observeByRace(raceId).map { list -> list.map { it.toDomain(teamDAO, driverDAO, carDAO) } }


    override suspend fun setSessionDriver(sessionId: Long, driverId: Long) =
            withContext(Dispatchers.IO) { dao.setDriver(sessionId, driverId) }

    override suspend fun setSessionCar(sessionId: Long, carId: Long) =
            withContext(Dispatchers.IO) { dao.setCar(sessionId, carId) }

    override suspend fun startRace(time: Long) = withContext(Dispatchers.IO) { dao.startRace(time) }
    override suspend fun stopRace(time: Long) = withContext(Dispatchers.IO) { dao.stopRace(time) }
    override suspend fun resetRace() = withContext(Dispatchers.IO) { dao.resetRace() }

    override suspend fun newSession(type: Session.Type, teamId: Long): Session = withContext(Dispatchers.IO) {
        val id = dao.save(SessionEntity(teamId = teamId, type = type.name))
        Session(id = id, teamId = teamId, type = type)
    }

    override suspend fun newSessions(type: Session.Type, vararg teamIds: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun startSessions(raceStartTime: Long, startTime: Long, vararg sessionIds: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun startNewSessions(raceStartTime: Long, startTime: Long, type: Session.Type, vararg teamIds: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun startNewSession(teamId: Long, raceStartTime: Long, startTime: Long, type: Session.Type, driverId: Long?): Session {
        val entity = SessionEntity(teamId = teamId,
                raceStartTime = raceStartTime,
                startDurationTime = startTime,
                type = type.name)
        val id = dao.save(entity)
        return entity.copy(id = id).toDomain(teamDAO, driverDAO, carDAO)
    }

    override suspend fun closeSessions(stopTime: Long, vararg sessionIds: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun removeLastSession(teamId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun clear() = withContext(Dispatchers.IO) { dao.clear() }


    //    override fun newSessions(type: Session.Type, vararg teamIds: Int): Observable<Session> =
    //            Observable.create { subscriber: ObservableEmitter<SessionEntity> ->
    //                for (teamId in teamIds) {
    //                    val sessionDb = SessionEntity(teamId)
    //                    sessionDb.type = type.name
    //                    subscriber.onNext(sessionDb)
    //                }
    //                subscriber.onComplete()
    //            }
    //                    .toList()
    //                    .flatMapObservable { dbStorage.add(it) }.map { it.data }
    //                    .flatMapSingle { transform(it) }
    //
    //    override fun startSessions(raceStartTime: Long, startTime: Long, vararg sessionIds: Int): Observable<Session> =
    //            OpenSessionOperation.from(raceStartTime, startTime, *sessionIds)
    //                    .toList()
    //                    .flatMapObservable { dbStorage.applyOpenOperations(it) }
    //                    .flatMap { get(it) }
    //
    //    override fun startNewSessions(raceStartTime: Long, startTime: Long, type: Session.Type, vararg teamIds: Int): Observable<Session> =
    //            Observable.create { subscriber: ObservableEmitter<SessionEntity> ->
    //                for (teamId in teamIds) {
    //                    val sessionDb = SessionEntity(teamId)
    //                    sessionDb.raceStartTime = raceStartTime
    //                    sessionDb.startDurationTime = startTime
    //                    sessionDb.type = type.name
    //                    subscriber.onNext(sessionDb)
    //                }
    //                subscriber.onComplete()
    //            }
    //                    .toList()
    //                    .flatMapObservable { dbStorage.add(it) }.map { it.data }
    //                    .flatMapSingle { transform(it) }
    //
    //    override fun startNewSession(raceStartTime: Long, startTime: Long, type: Session.Type, driverId: Int, teamId: Int): Observable<Session> =
    //            Observable.create { subscriber: ObservableEmitter<SessionEntity> ->
    //                val sessionDb = SessionEntity(teamId)
    //                sessionDb.raceStartTime = raceStartTime
    //                sessionDb.startDurationTime = startTime
    //                sessionDb.driverId = driverId
    //                sessionDb.type = type.name
    //                subscriber.onNext(sessionDb)
    //                subscriber.onComplete()
    //            }
    //                    .toList()
    //                    .flatMapObservable { dbStorage.add(it) }.map { it.data }
    //                    .flatMapSingle { transform(it) }
    //
    //    override fun closeSessions(stopTime: Long, vararg sessionIds: Int): Observable<Session> =
    //            CloseSessionOperation.from(stopTime, *sessionIds)
    //                    .toList()
    //                    .flatMapObservable { dbStorage.applyCloseOperations(it) }
    //                    .flatMap { get(it) }
    //
    //    override fun removeLastSession(teamId: Int): Observable<Session> =
    //            dbStorage.getByTeamId(teamId)
    //                    .toSortedList { o1, o2 -> o1.startDurationTime.compareTo(o2.startDurationTime) }
    //                    .flatMapObservable { list ->
    //                        val count = list.size
    //                        when {
    //                            count >= 2 -> dbStorage.getByTeamId(teamId)
    //                                    .toSortedList { o1, o2 -> o1.startDurationTime.compareTo(o2.startDurationTime) }
    //                                    .flatMapObservable {
    //                                        Single.zip(
    //                                                zipOpenSession(it[count - 2]),
    //                                                removeSession(it[count - 1]),
    //                                                BiFunction { t1: SessionEntity, _: Int -> t1 }
    //                                        ).toObservable()
    //                                    }
    //
    //                            count == 1 ->
    //                                dbStorage.getByTeamId(teamId)
    //                                        .toSortedList { o1, o2 -> o1.startDurationTime.compareTo(o2.startDurationTime) }
    //                                        .flatMapObservable { Observable.just(it.first()) }
    //                            else -> throw RuntimeException("No sessions in the database")
    //                        }
    //                    }
    //                    .flatMapSingle { transform(it) }
    //
    //    private fun zipOpenSession(sessionDb: SessionEntity): Single<SessionEntity> = Single.zip(
    //            Single.just(sessionDb),
    //            openSession(sessionDb.id, sessionDb.raceStartTime, sessionDb.startDurationTime),
    //            BiFunction { sessionDb1, _ -> sessionDb1 }
    //    )
    //
    //    private fun openSession(id: Int, raceStartTime: Long, startTime: Long): Single<Int> =
    //            Observable.just(OpenSessionOperation(id, raceStartTime, startTime))
    //                    .toList()
    //                    .flatMapObservable { dbStorage.applyOpenOperations(it) }
    //                    .single(0)
    //
    //    private fun removeSession(sessionDb: SessionEntity): Single<Int> = dbStorage.remove(sessionDb)
}