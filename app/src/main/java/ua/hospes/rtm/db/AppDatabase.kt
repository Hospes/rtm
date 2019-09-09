package ua.hospes.rtm.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.hospes.rtm.db.cars.CarDAO
import ua.hospes.rtm.db.cars.CarEntity
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.drivers.DriverEntity
import ua.hospes.rtm.db.team.TeamDAO
import ua.hospes.rtm.db.team.TeamEntity

@Database(entities = [CarEntity::class, TeamEntity::class, DriverEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDAO
    abstract fun teamDao(): TeamDAO
    abstract fun driverDao(): DriverDAO
}