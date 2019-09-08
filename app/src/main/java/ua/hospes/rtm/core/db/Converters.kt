package ua.hospes.rtm.core.db

import androidx.room.TypeConverter
import ua.hospes.rtm.domain.cars.Car

class Converters {
    @TypeConverter
    fun fromCarQualityEnum(quality: Car.Quality): String = quality.name

    @TypeConverter
    fun toCarQualityEnum(name: String): Car.Quality = Car.Quality.fromString(name)
}