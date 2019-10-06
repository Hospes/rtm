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

    override suspend fun get(): List<Session> =
            withContext(Dispatchers.IO) { dao.get().map { it.toDomain(teamDAO, driverDAO, carDAO) } }

    override suspend fun get(vararg ids: Long): List<Session> =
            withContext(Dispatchers.IO) { dao.get(*ids).map { it.toDomain(teamDAO, driverDAO, carDAO) } }

    override suspend fun getByTeam(teamId: Long): List<Session> =
            withContext(Dispatchers.IO) { dao.getByTeam(teamId).map { it.toDomain(teamDAO, driverDAO, carDAO) } }

    override suspend fun getByTeamAndDriver(teamId: Long, driverId: Long): List<Session> =
            withContext(Dispatchers.IO) { dao.getByTeamAndDriver(teamId, driverId).map { it.toDomain(teamDAO, driverDAO, carDAO) } }


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

    override suspend fun closeCurrentStartNew(raceItemId: Long, currentTime: Long, type: Session.Type) =
            withContext(Dispatchers.IO) { dao.closeCurrentStartNew(raceItemId, currentTime, type) }

    override suspend fun removeLastSession(teamId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun clear() = withContext(Dispatchers.IO) { dao.clear() }

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
}