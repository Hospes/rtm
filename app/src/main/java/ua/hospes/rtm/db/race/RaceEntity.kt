package ua.hospes.rtm.db.race

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.team.TeamDAO
import ua.hospes.rtm.domain.race.models.RaceItem

@Entity(tableName = "race")
data class RaceEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "team_id") val teamId: Int,
        @ColumnInfo(name = "team_number") val teamNumber: Int,
        @ColumnInfo(name = "session_id") val sessionId: Int? = null,
        @ColumnInfo(name = "order") val order: Int = 0
) {
    internal suspend fun toDomain(teamDAO: TeamDAO, driverDAO: DriverDAO): RaceItem =
            RaceItem(id, teamNumber, teamDAO.get(teamId).toDomain(driverDAO))
}