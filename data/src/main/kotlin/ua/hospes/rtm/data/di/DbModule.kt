package ua.hospes.rtm.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.hospes.rtm.data.BuildConfig
import ua.hospes.rtm.data.db.AppDatabase
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase = Room.databaseBuilder(ctx, AppDatabase::class.java, "rtm")
        .apply { if (BuildConfig.DEBUG) addCallback(prepopulateData) }
        .build()

    private val prepopulateData = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Executors.newSingleThreadScheduledExecutor().execute {
                db.execSQL(
                    """INSERT INTO cars (number,quality,is_broken)
                                                    VALUES (4,"NORMAL",0), (5,"NORMAL",0),
                                                    (6,"NORMAL",0), (7,"NORMAL",0),
                                                    (13,"NORMAL",0), (15,"NORMAL",0),
                                                    (16,"NORMAL",0), (27,"NORMAL",0),
                                                    (33,"NORMAL",0), (45,"NORMAL",0)"""
                )

                db.execSQL(
                    """INSERT INTO teams (name)
                                                    VALUES ("Fast&Furious"), ("BestFriends"),
                                                    ("Blade Runners"), ("God+1"),
                                                    ("PotatoPCs"), ("Gremlings")"""
                )

                db.execSQL(
                    """INSERT INTO drivers (name,team_id)
                                                    VALUES ("Almaz",1), ("Andrew",1),
                                                    ("Leprok",2), ("Vasya",2),
                                                    ("Sergey",3), ("Popovich",3)"""
                )
            }
        }
    }
}