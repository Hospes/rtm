package ua.hospes.rtm.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.hospes.rtm.core.base.util.AppCoroutineDispatchers
import ua.hospes.rtm.data.db.AppDatabase
import ua.hospes.rtm.data.db.drivers.DriverDAO
import ua.hospes.rtm.data.db.team.TeamDAO
import ua.hospes.rtm.data.model.DriverDto
import ua.hospes.rtm.data.model.toDbEntity
import ua.hospes.rtm.data.model.toDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriversRepository @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    db: AppDatabase,
) {
    private val dispatcher = dispatchers.io
    private val driverDAO: DriverDAO = db.driverDao()
    private val teamDAO: TeamDAO = db.teamDao()


    suspend fun get(): List<DriverDto> = withContext(dispatcher) { driverDAO.get().map { it.toDto(teamDAO) } }

    suspend fun get(vararg ids: Long): List<DriverDto> = withContext(dispatcher) { driverDAO.get(*ids).map { it.toDto(teamDAO) } }

    suspend fun getNotInRace(teamId: Long): List<DriverDto> = withContext(dispatcher) { driverDAO.getNotSelected(teamId).map { it.toDto(teamDAO) } }

    fun listen(): Flow<List<DriverDto>> = driverDAO.observe().map { list -> list.map { it.toDto(teamDAO) } }


    suspend fun addDriversToTeam(teamId: Long, vararg driverIds: Long) = withContext(dispatcher) { driverDAO.addDriversToTeam(teamId, driverIds) }

    suspend fun removeDriversFromTeam(teamId: Long) = withContext(dispatcher) { driverDAO.removeDriversFromTeam(teamId) }


    suspend fun save(driver: DriverDto): DriverDto = withContext(dispatcher) { driverDAO.save(driver.toDbEntity()).let { driver.copy(id = it) } }

    suspend fun delete(id: Long) = withContext(dispatcher) { driverDAO.delete(id) }

    suspend fun clear() = withContext(dispatcher) { driverDAO.clear() }
}