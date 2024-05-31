package ua.hospes.rtm.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.hospes.rtm.data.db.AppDatabase
import ua.hospes.rtm.data.db.drivers.DriverDAO
import ua.hospes.rtm.data.db.team.TeamDAO
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.toDbEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamsRepository @Inject constructor(db: AppDatabase) {
    private val dao: TeamDAO = db.teamDao()
    private val driverDAO: DriverDAO = db.driverDao()


    suspend fun get(): List<Team> =
        withContext(Dispatchers.IO) { dao.get().map { it.toDomain(driverDAO) } }

    suspend fun getNotInRace(): List<Team> =
        withContext(Dispatchers.IO) { dao.getNotInRace().map { it.toDomain(driverDAO) } }


    fun listen(): Flow<List<Team>> = dao.observe().map { list -> list.map { it.toDomain(driverDAO) } }


    suspend fun save(team: Team) =
        withContext(Dispatchers.IO) { dao.save(team.toDbEntity(), team.drivers.map { it.id }.toLongArray()) }

    suspend fun delete(id: Long) =
        withContext(Dispatchers.IO) { dao.delete(id) }

    suspend fun clear() =
        withContext(Dispatchers.IO) { dao.clear() }
}