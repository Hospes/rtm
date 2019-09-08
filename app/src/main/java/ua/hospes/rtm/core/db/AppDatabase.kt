package ua.hospes.rtm.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.hospes.rtm.core.db.cars.CarDAO
import ua.hospes.rtm.core.db.cars.CarEntity
import ua.hospes.rtm.core.db.drivers.DriverDAO
import ua.hospes.rtm.core.db.drivers.DriverEntity
import ua.hospes.rtm.core.db.team.TeamDAO
import ua.hospes.rtm.core.db.team.TeamEntity

@Database(entities = [CarEntity::class, TeamEntity::class, DriverEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDAO
    abstract fun teamDao(): TeamDAO
    abstract fun driverDao(): DriverDAO
}