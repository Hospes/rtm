package ua.hospes.rtm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.drivers.toDomain
import ua.hospes.rtm.db.team.TeamDAO
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.domain.drivers.toDbEntity

internal class DriversRepositoryImpl(private val dao: DriverDAO, private val teamDAO: TeamDAO) : DriversRepository {
    override suspend fun get(): List<Driver> =
            withContext(Dispatchers.IO) { dao.get().map { it.toDomain(teamDAO) } }

    override suspend fun get(vararg ids: Int): List<Driver> =
            withContext(Dispatchers.IO) { dao.getByIds(ids).map { it.toDomain(teamDAO) } }

    override suspend fun getByTeamId(teamId: Int): List<Driver> =
            withContext(Dispatchers.IO) { dao.getByTeamId(teamId).map { it.toDomain(teamDAO) } }


    override fun listen(): Flow<List<Driver>> = dao.observe().map { list -> list.map { it.toDomain(teamDAO) } }


    override suspend fun addDriversToTeam(teamId: Int, vararg driverIds: Int) =
            withContext(Dispatchers.IO) { dao.addDriversToTeam(teamId, driverIds) }

    override suspend fun removeDriversFromTeam(teamId: Int) =
            withContext(Dispatchers.IO) { dao.removeDrviersFromTeam(teamId) }


    override suspend fun save(driver: Driver) =
            withContext(Dispatchers.IO) { dao.save(driver.toDbEntity()) }

    override suspend fun delete(id: Int) =
            withContext(Dispatchers.IO) { dao.delete(intArrayOf(id)) }

    override suspend fun clear() =
            withContext(Dispatchers.IO) { dao.clear() }
}