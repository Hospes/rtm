package ua.hospes.rtm.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import ua.hospes.rtm.data.db.AppDatabase
import ua.hospes.rtm.data.db.cars.CarDAO
import ua.hospes.rtm.data.db.drivers.DriverDAO
import ua.hospes.rtm.data.db.race.RaceDAO
import ua.hospes.rtm.data.db.sessions.SessionDAO
import ua.hospes.rtm.data.db.team.TeamDAO
import ua.hospes.rtm.data.model.RaceDto
import ua.hospes.rtm.data.model.toDto
import ua.hospes.rtm.data.model.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RaceRepository @Inject constructor(db: AppDatabase) {
    private val dao: RaceDAO = db.raceDao()
    private val teamDAO: TeamDAO = db.teamDao()
    private val driverDAO: DriverDAO = db.driverDao()
    private val carDAO: CarDAO = db.carDao()
    private val sessionDAO: SessionDAO = db.sessionDao()


    fun listen(): Flow<List<RaceDto>> = dao.observe()
        .map { list -> list.map { it.toDto(teamDAO, driverDAO, carDAO, sessionDAO) } }

    fun listen(id: Long): Flow<RaceDto> = dao.observe(id)
        .map { it.toDto(teamDAO, driverDAO, carDAO, sessionDAO) }

    suspend fun save(race: RaceDto) = withContext(Dispatchers.IO) {
        Timber.d("Save: $race | Entity: ${race.toEntity()}")
        dao.save(race.toEntity()).let { }
    }

    suspend fun clear() = withContext(Dispatchers.IO) {
        dao.clear()
        sessionDAO.clear()
    }
}