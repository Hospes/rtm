package ua.hospes.rtm.domain.team

import android.os.Parcelable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import ua.hospes.rtm.data.db.drivers.DriverDAO
import ua.hospes.rtm.data.db.team.TeamEntity
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.drivers.toDomain

@Parcelize
data class Team(
    val id: Long = 0,
    val name: String,
    val drivers: List<Driver> = emptyList()
) : Parcelable

internal suspend fun TeamEntity.toDomain(dao: DriverDAO): Team = withContext(Dispatchers.IO) {
    Team(id, name, dao.getByTeamId(id).map { it.toDomain(this@TeamEntity) }.toMutableList())
}

fun Team.toDbEntity(): TeamEntity = TeamEntity(id, name)