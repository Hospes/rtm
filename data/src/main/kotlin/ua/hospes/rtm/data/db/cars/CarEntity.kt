package ua.hospes.rtm.data.db.cars

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
internal data class CarEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "number") val number: Int,
    @ColumnInfo(name = "quality") val quality: Quality = Quality.NORMAL,
    @ColumnInfo(name = "is_broken") val broken: Boolean = false
) {
    enum class Quality {
        LOW,
        NORMAL,
        HIGH;

        companion object {
            fun fromString(s: String): Quality = entries.find { it.name.equals(s, ignoreCase = true) } ?: NORMAL
        }
    }
}