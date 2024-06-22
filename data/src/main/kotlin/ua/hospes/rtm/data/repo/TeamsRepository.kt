package ua.hospes.rtm.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.hospes.rtm.core.base.util.AppCoroutineDispatchers
import ua.hospes.rtm.data.db.AppDatabase
import ua.hospes.rtm.data.db.drivers.DriverDAO
import ua.hospes.rtm.data.db.team.TeamDAO
import ua.hospes.rtm.data.model.TeamDto
import ua.hospes.rtm.data.model.toDbEntity
import ua.hospes.rtm.data.model.toDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamsRepository @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    db: AppDatabase,
) {
    private val dispatcher = dispatchers.io
    private val dao: TeamDAO = db.teamDao()
    private val driverDAO: DriverDAO = db.driverDao()


    suspend fun get(): List<TeamDto> = withContext(dispatcher) { dao.get().map { it.toDto(driverDAO) } }

    suspend fun getNotInRace(): List<TeamDto> = withContext(dispatcher) { dao.getNotInRace().map { it.toDto(driverDAO) } }


    fun listen(): Flow<List<TeamDto>> = dao.observe().map { list -> list.map { it.toDto(driverDAO) } }


    suspend fun save(team: TeamDto) = withContext(dispatcher) { dao.save(team.toDbEntity(), team.drivers.map { it.id }.toLongArray()) }

    suspend fun delete(id: Long) = withContext(dispatcher) { dao.delete(id) }

    suspend fun clear() = withContext(dispatcher) { dao.clear() }
}