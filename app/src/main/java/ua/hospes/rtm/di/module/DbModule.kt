package ua.hospes.rtm.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import ua.hospes.rtm.BuildConfig
import ua.hospes.rtm.db.AppDatabase
import ua.hospes.rtm.db.cars.CarDAO
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.race.RaceDAO
import ua.hospes.rtm.db.sessions.SessionDAO
import ua.hospes.rtm.db.team.TeamDAO
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
internal object DbModule {
    @Provides @Singleton @JvmStatic
    fun provideDb(ctx: Context): AppDatabase = Room.databaseBuilder(ctx, AppDatabase::class.java, "rtm")
            .apply { if (BuildConfig.DEBUG) addCallback(prepopulateData) }
            .build()

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


    private val prepopulateData = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Executors.newSingleThreadScheduledExecutor().execute {
                db.execSQL("""INSERT INTO cars (number,quality,is_broken)
                                                    VALUES (4,"NORMAL",0), (5,"NORMAL",0),
                                                    (6,"NORMAL",0), (7,"NORMAL",0),
                                                    (13,"NORMAL",0), (15,"NORMAL",0),
                                                    (16,"NORMAL",0), (27,"NORMAL",0),
                                                    (33,"NORMAL",0), (45,"NORMAL",0)""")

                db.execSQL("""INSERT INTO teams (name)
                                                    VALUES ("Fast&Furious"), ("BestFriends"),
                                                    ("Blade Runners"), ("God+1"),
                                                    ("PotatoPCs"), ("Gremlings")""")

                db.execSQL("""INSERT INTO drivers (name,team_id)
                                                    VALUES ("Almaz",1), ("Andrew",1),
                                                    ("Leprok",2), ("Vasya",2),
                                                    ("Sergey",3), ("Popovich",3)""")
            }
        }
    }
}