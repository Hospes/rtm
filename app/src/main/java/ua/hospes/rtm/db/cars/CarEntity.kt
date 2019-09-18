package ua.hospes.rtm.db.cars

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.hospes.rtm.domain.cars.Car

@Entity(tableName = "cars")
data class CarEntity(
        @PrimaryKey(autoGenerate = true) val uid: Long = 0,
        @ColumnInfo(name = "number") val number: Int,
        @ColumnInfo(name = "quality") val quality: Car.Quality = Car.Quality.NORMAL,
        @ColumnInfo(name = "is_broken") val broken: Boolean = false
)

fun CarEntity.toDomain(): Car = Car(uid, number, quality, broken)