package ua.hospes.rtm.data.db.race

import androidx.room.*
import ua.hospes.rtm.data.db.sessions.SessionEntity
import ua.hospes.rtm.data.db.team.TeamEntity

@Entity(
    tableName = "race",
    indices = [Index(value = ["team_id"]), Index(value = ["session_id"])],
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["session_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_NULL
        ),

        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["team_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
internal data class RaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "team_id") val teamId: Long,
    @ColumnInfo(name = "team_number") val teamNumber: Int,
    @ColumnInfo(name = "session_id") val sessionId: Long? = null,
    @ColumnInfo(name = "order") val order: Int = 0
)