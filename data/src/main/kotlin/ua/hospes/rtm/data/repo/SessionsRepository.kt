package ua.hospes.rtm.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.hospes.rtm.core.base.util.AppCoroutineDispatchers
import ua.hospes.rtm.data.db.AppDatabase
import ua.hospes.rtm.data.db.cars.CarDAO
import ua.hospes.rtm.data.db.drivers.DriverDAO
import ua.hospes.rtm.data.db.sessions.SessionDAO
import ua.hospes.rtm.data.db.sessions.SessionEntity
import ua.hospes.rtm.data.db.team.TeamDAO
import ua.hospes.rtm.data.model.SessionDto
import ua.hospes.rtm.data.model.toDto
import ua.hospes.rtm.data.model.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionsRepository @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    db: AppDatabase,
) {
    private val dispatcher = dispatchers.io
    private val dao: SessionDAO = db.sessionDao()
    private val teamDAO: TeamDAO = db.teamDao()
    private val driverDAO: DriverDAO = db.driverDao()
    private val carDAO: CarDAO = db.carDao()
    private val raceDAO = db.raceDao()

    suspend fun get(): List<SessionDto> = withContext(dispatcher) { dao.get().map { it.toDto(teamDAO, driverDAO, carDAO) } }

    suspend fun getByTeam(teamId: Long): List<SessionDto> =
        withContext(dispatcher) { dao.getByTeam(teamId).map { it.toDto(teamDAO, driverDAO, carDAO) } }


    fun listenByRaceId(raceId: Long): Flow<List<SessionDto>> =
        dao.observeByRace(raceId).map { list -> list.map { it.toDto(teamDAO, driverDAO, carDAO) } }


    suspend fun setSessionDriver(sessionId: Long, driverId: Long) =
        withContext(dispatcher) { dao.setDriver(sessionId, driverId) }

    suspend fun setSessionCar(sessionId: Long, carId: Long) =
        withContext(dispatcher) { dao.setCar(sessionId, carId) }

    suspend fun startRace(time: Long) = withContext(dispatcher) { dao.startRace(time) }
    suspend fun stopRace(time: Long) = withContext(dispatcher) { dao.stopRace(time) }
    suspend fun resetRace() = withContext(dispatcher) { dao.resetRace() }


    suspend fun newSession(type: SessionDto.Type, teamId: Long): SessionDto = withContext(dispatcher) {
        val id = dao.save(SessionEntity(teamId = teamId, type = type.toEntity()))
        SessionDto(id = id, teamId = teamId, type = type)
    }

    suspend fun closeCurrentStartNew(raceItemId: Long, currentTime: Long, type: SessionDto.Type) =
        withContext(dispatcher) { dao.closeCurrentStartNew(raceItemId, currentTime, type.toEntity()) }

    suspend fun clear() = withContext(dispatcher) { dao.clear() }

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