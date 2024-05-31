package ua.hospes.rtm.data.db

import androidx.room.TypeConverter
import ua.hospes.rtm.data.db.cars.CarEntity
import ua.hospes.rtm.data.db.sessions.SessionEntity

class Converters {
    @TypeConverter
    fun fromCarQualityEnum(quality: CarEntity.Quality): String = quality.name

    @TypeConverter
    fun toCarQualityEnum(name: String): CarEntity.Quality = CarEntity.Quality.fromString(name)


    @TypeConverter
    fun fromSessionTypeEnum(type: SessionEntity.Type): String = type.name

    @TypeConverter
    fun toSessionTypeEnum(name: String): SessionEntity.Type = SessionEntity.Type.fromString(name)
}