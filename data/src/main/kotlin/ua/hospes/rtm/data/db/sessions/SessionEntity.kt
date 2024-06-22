package ua.hospes.rtm.data.db.sessions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
internal data class SessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "team_id") val teamId: Long,
    @ColumnInfo(name = "driver_id") val driverId: Long? = null,
    @ColumnInfo(name = "car_id") val carId: Long? = null,
    @ColumnInfo(name = "race_start_time") val raceStartTime: Long? = null,
    @ColumnInfo(name = "start_time") val startTime: Long? = null,
    @ColumnInfo(name = "end_time") val endTime: Long? = null,
    @ColumnInfo(name = "type") val type: Type
) {
    enum class Type {
        TRACK,
        PIT;

        companion object {
            fun fromString(s: String): Type = Type.entries.find { it.name.equals(s, ignoreCase = true) } ?: TRACK
        }
    }
}