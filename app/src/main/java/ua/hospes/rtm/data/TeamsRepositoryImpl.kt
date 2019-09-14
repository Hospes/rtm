package ua.hospes.rtm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.team.TeamDAO
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.TeamsRepository
import ua.hospes.rtm.domain.team.toDbEntity

internal class TeamsRepositoryImpl(private val dao: TeamDAO, private val driverDAO: DriverDAO) : TeamsRepository {
    override suspend fun get(): List<Team> =
            withContext(Dispatchers.IO) { dao.get().map { it.toDomain(driverDAO) } }

    override suspend fun get(vararg ids: Int): List<Team> =
            withContext(Dispatchers.IO) { dao.get(ids).map { it.toDomain(driverDAO) } }

    override suspend fun getNotInRace(): List<Team> =
            withContext(Dispatchers.IO) { dao.getNotInRace().map { it.toDomain(driverDAO) } }


    override fun listen(): Flow<List<Team>> = dao.observe().map { list -> list.map { it.toDomain(driverDAO) } }


    override suspend fun save(team: Team) =
            withContext(Dispatchers.IO) {
                dao.save(team.toDbEntity())
                driverDAO.removeDriversFromTeam(team.id)
                driverDAO.addDriversToTeam(team.id, team.drivers.map { it.id }.toIntArray())
            }

    override suspend fun delete(id: Int) =
            withContext(Dispatchers.IO) { dao.delete(intArrayOf(id)) }

    override suspend fun clear() =
            withContext(Dispatchers.IO) { dao.clear() }
}