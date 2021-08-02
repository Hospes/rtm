package ua.hospes.rtm.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.hospes.rtm.db.cars.CarDAO
import ua.hospes.rtm.db.cars.CarEntity
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.drivers.DriverEntity
import ua.hospes.rtm.db.race.RaceDAO
import ua.hospes.rtm.db.race.RaceEntity
import ua.hospes.rtm.db.sessions.SessionDAO
import ua.hospes.rtm.db.sessions.SessionEntity
import ua.hospes.rtm.db.team.TeamDAO
import ua.hospes.rtm.db.team.TeamEntity

@Database(
    version = 1, exportSchema = true,
    entities = [CarEntity::class, TeamEntity::class, DriverEntity::class, SessionEntity::class, RaceEntity::class]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDAO
    abstract fun teamDao(): TeamDAO
    abstract fun driverDao(): DriverDAO
    abstract fun sessionDao(): SessionDAO
    abstract fun raceDao(): RaceDAO
}