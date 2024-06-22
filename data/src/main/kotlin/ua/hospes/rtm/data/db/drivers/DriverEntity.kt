package ua.hospes.rtm.data.db.drivers

import androidx.room.*
import ua.hospes.rtm.data.db.team.TeamEntity

@Entity(
    tableName = "drivers",
    indices = [Index(value = ["team_id"])],
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["team_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
internal data class DriverEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "team_id") val teamId: Long? = null
)