package ua.hospes.rtm.db.race

import androidx.room.*
import ua.hospes.rtm.db.cars.CarDAO
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.sessions.SessionDAO
import ua.hospes.rtm.db.sessions.SessionEntity
import ua.hospes.rtm.db.sessions.toDomain
import ua.hospes.rtm.db.team.TeamDAO
import ua.hospes.rtm.db.team.TeamEntity
import ua.hospes.rtm.domain.race.models.RaceItem

@Entity(tableName = "race",
        indices = [Index(value = ["team_id", "session_id"])],
        foreignKeys = [
            ForeignKey(entity = SessionEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["session_id"],
                    onUpdate = ForeignKey.CASCADE,
                    onDelete = ForeignKey.SET_NULL),

            ForeignKey(entity = TeamEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["team_id"],
                    onUpdate = ForeignKey.CASCADE,
                    onDelete = ForeignKey.SET_NULL)
        ])
data class RaceEntity(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        @ColumnInfo(name = "team_id") val teamId: Long,
        @ColumnInfo(name = "team_number") val teamNumber: Int,
        @ColumnInfo(name = "session_id") val sessionId: Long? = null,
        @ColumnInfo(name = "order") val order: Int = 0
) {
    internal suspend fun toDomain(teamDAO: TeamDAO, driverDAO: DriverDAO, carDAO: CarDAO, sessionDAO: SessionDAO): RaceItem =
            RaceItem(
                    id = id,
                    teamNumber = teamNumber,
                    team = teamDAO.get(teamId).toDomain(driverDAO),
                    session = sessionId?.let { sessionDAO.get(it) }?.toDomain(teamDAO, driverDAO, carDAO),
                    details = null
            )
}