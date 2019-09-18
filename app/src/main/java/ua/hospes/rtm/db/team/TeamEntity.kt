package ua.hospes.rtm.db.team

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.drivers.toDomain
import ua.hospes.rtm.domain.team.Team

@Entity(tableName = "teams")
data class TeamEntity(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        @ColumnInfo(name = "name") val name: String
) {
    internal suspend fun toDomain(dao: DriverDAO): Team = withContext(Dispatchers.IO) {
        Team(id, name, dao.getByTeamId(id).map { it.toDomain(this@TeamEntity) }.toMutableList())
    }
}