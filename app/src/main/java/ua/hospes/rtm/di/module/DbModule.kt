package ua.hospes.rtm.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ua.hospes.rtm.db.AppDatabase
import ua.hospes.rtm.db.cars.CarDAO
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.race.RaceDAO
import ua.hospes.rtm.db.sessions.SessionDAO
import ua.hospes.rtm.db.team.TeamDAO
import javax.inject.Singleton

@Module
internal object DbModule {
    @Provides @Singleton @JvmStatic
    fun provideDb(ctx: Context): AppDatabase = Room.databaseBuilder(ctx, AppDatabase::class.java, "rtm").build()

    @Provides @JvmStatic
    fun provideCarDAO(db: AppDatabase): CarDAO = db.carDao()

    @Provides @JvmStatic
    fun provideTeamDAO(db: AppDatabase): TeamDAO = db.teamDao()

    @Provides @JvmStatic
    fun provideDriverDAO(db: AppDatabase): DriverDAO = db.driverDao()

    @Provides @JvmStatic
    fun provideSessionDAO(db: AppDatabase): SessionDAO = db.sessionDao()

    @Provides @JvmStatic
    fun provideRaceDAO(db: AppDatabase): RaceDAO = db.raceDao()
}