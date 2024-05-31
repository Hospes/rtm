package ua.hospes.rtm.domain.drivers

import android.os.Parcelable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import ua.hospes.rtm.data.db.drivers.DriverEntity
import ua.hospes.rtm.data.db.team.TeamDAO
import ua.hospes.rtm.data.db.team.TeamEntity

@Parcelize
data class Driver(
    val id: Long = 0,
    val name: String,
    val teamId: Long? = null,
    val teamName: String? = null
) : Parcelable

fun DriverEntity.toDomain(team: TeamEntity? = null): Driver = team?.let {
    require(teamId == it.id) { "Wrong team" }
    Driver(id, name, it.id, it.name)
} ?: Driver(id, name, teamId)

internal suspend fun DriverEntity.toDomain(dao: TeamDAO): Driver = withContext(Dispatchers.IO) {
    teamId?.let {
        val team = dao.get(*longArrayOf(it)).firstOrNull() ?: throw IllegalStateException("Didn't found team with given id[$it]")
        Driver(id, name, team.id, team.name)
    } ?: Driver(id, name)
}

fun Driver.toDbEntity(): DriverEntity = DriverEntity(id, name, teamId)