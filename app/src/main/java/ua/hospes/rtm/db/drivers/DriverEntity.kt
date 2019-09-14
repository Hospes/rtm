package ua.hospes.rtm.db.drivers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.hospes.rtm.db.team.TeamDAO
import ua.hospes.rtm.db.team.TeamEntity
import ua.hospes.rtm.domain.drivers.Driver

@Entity(tableName = "drivers")
data class DriverEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "team_id") val teamId: Int? = null
)

fun DriverEntity.toDomain(team: TeamEntity? = null): Driver = team?.let {
    require(teamId == it.id) { "Wrong team" }
    Driver(id, name, it.id, it.name)
} ?: Driver(id, name, teamId)

internal suspend fun DriverEntity.toDomain(dao: TeamDAO): Driver = withContext(Dispatchers.IO) {
    teamId?.let {
        val team = dao.get(intArrayOf(it)).firstOrNull() ?: throw IllegalStateException("Didn't found team with given id[$it]")
        Driver(id, name, team.id, team.name)
    } ?: Driver(id, name)
}