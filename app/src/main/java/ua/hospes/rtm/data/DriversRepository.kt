package ua.hospes.rtm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.hospes.rtm.db.AppDatabase
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.drivers.toDomain
import ua.hospes.rtm.db.team.TeamDAO
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.drivers.toDbEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriversRepository @Inject constructor(db: AppDatabase) {
    private val driverDAO: DriverDAO = db.driverDao()
    private val teamDAO: TeamDAO = db.teamDao()


    suspend fun get(): List<Driver> =
        withContext(Dispatchers.IO) { driverDAO.get().map { it.toDomain(teamDAO) } }

    suspend fun get(vararg ids: Long): List<Driver> =
        withContext(Dispatchers.IO) { driverDAO.get(*ids).map { it.toDomain(teamDAO) } }

    suspend fun getNotInRace(teamId: Long): List<Driver> =
        withContext(Dispatchers.IO) { driverDAO.getNotSelected(teamId).map { it.toDomain(teamDAO) } }

    fun listen(): Flow<List<Driver>> = driverDAO.observe().map { list -> list.map { it.toDomain(teamDAO) } }


    suspend fun addDriversToTeam(teamId: Long, vararg driverIds: Long) =
        withContext(Dispatchers.IO) { driverDAO.addDriversToTeam(teamId, driverIds) }

    suspend fun removeDriversFromTeam(teamId: Long) =
        withContext(Dispatchers.IO) { driverDAO.removeDriversFromTeam(teamId) }


    suspend fun save(driver: Driver): Driver =
        withContext(Dispatchers.IO) { driverDAO.save(driver.toDbEntity()).let { driver.copy(id = it) } }

    suspend fun delete(id: Long) =
        withContext(Dispatchers.IO) { driverDAO.delete(id) }

    suspend fun clear() =
        withContext(Dispatchers.IO) { driverDAO.clear() }
}