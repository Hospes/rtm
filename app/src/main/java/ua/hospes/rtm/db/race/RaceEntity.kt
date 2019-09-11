package ua.hospes.rtm.db.race

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "race")
class RaceEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "team_id") val teamId: Int,
        @ColumnInfo(name = "team_number") var teamNumber: Int,
        @ColumnInfo(name = "session_id") var sessionId: Int? = null,
        @ColumnInfo(name = "order") var order: Int = 0
)