package ua.hospes.rtm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.hospes.rtm.data.db.cars.CarDAO
import ua.hospes.rtm.data.db.cars.CarEntity
import ua.hospes.rtm.data.db.drivers.DriverDAO
import ua.hospes.rtm.data.db.drivers.DriverEntity
import ua.hospes.rtm.data.db.race.RaceDAO
import ua.hospes.rtm.data.db.race.RaceEntity
import ua.hospes.rtm.data.db.sessions.SessionDAO
import ua.hospes.rtm.data.db.sessions.SessionEntity
import ua.hospes.rtm.data.db.team.TeamDAO
import ua.hospes.rtm.data.db.team.TeamEntity

@Database(
    version = 1, exportSchema = true,
    entities = [CarEntity::class, TeamEntity::class, DriverEntity::class, SessionEntity::class, RaceEntity::class]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    internal abstract fun carDao(): CarDAO
    internal abstract fun teamDao(): TeamDAO
    internal abstract fun driverDao(): DriverDAO
    internal abstract fun sessionDao(): SessionDAO
    internal abstract fun raceDao(): RaceDAO
}