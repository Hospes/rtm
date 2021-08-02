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
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.race.models.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RaceRepository @Inject constructor(db: AppDatabase) {
    private val dao: RaceDAO = db.raceDao()
    private val teamDAO: TeamDAO = db.teamDao()
    private val driverDAO: DriverDAO = db.driverDao()
    private val carDAO: CarDAO = db.carDao()
    private val sessionDAO: SessionDAO = db.sessionDao()


    fun listen(): Flow<List<RaceItem>> = dao.observe()
        .map { list -> list.map { it.toDomain(teamDAO, driverDAO, carDAO, sessionDAO) } }

    fun listen(id: Long): Flow<RaceItem> = dao.observe(id)
        .map { it.toDomain(teamDAO, driverDAO, carDAO, sessionDAO) }

    suspend fun save(race: RaceItem) = withContext(Dispatchers.IO) {
        Timber.d("Save: $race | Entity: ${race.toEntity()}")
        dao.save(race.toEntity()).let { }
    }

    suspend fun clear() = withContext(Dispatchers.IO) {
        dao.clear()
        sessionDAO.clear()
    }
}