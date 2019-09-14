package ua.hospes.rtm.di.module

import dagger.Module
import dagger.Provides
import ua.hospes.rtm.data.*
import ua.hospes.rtm.db.AppDatabase
import ua.hospes.rtm.db.cars.CarDAO
import ua.hospes.rtm.db.drivers.DriverDAO
import ua.hospes.rtm.db.team.TeamDAO
import ua.hospes.rtm.domain.cars.CarsRepository
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.sessions.SessionsRepository
import ua.hospes.rtm.domain.team.TeamsRepository
import javax.inject.Singleton

@Module
object RepoModule {
    @Provides @Singleton @JvmStatic
    internal fun provideRaceRepository(db: AppDatabase)
            : RaceRepository = RaceRepositoryImpl(db)

    @Provides @Singleton @JvmStatic
    internal fun provideSessionsRepository(db: AppDatabase)
            : SessionsRepository = SessionsRepositoryImpl(db.sessionDao(), db.teamDao(), db.driverDao(), db.carDao())

    @Provides @Singleton @JvmStatic
    internal fun provideDriversRepository(dao: DriverDAO, teamDAO: TeamDAO)
            : DriversRepository = DriversRepositoryImpl(dao, teamDAO)

    @Provides @Singleton @JvmStatic
    internal fun provideTeamsRepository(dao: TeamDAO, driverDAO: DriverDAO)
            : TeamsRepository = TeamsRepositoryImpl(dao, driverDAO)

    @Provides @Singleton @JvmStatic
    internal fun provideCarsRepository(dao: CarDAO)
            : CarsRepository = CarsRepositoryImpl(dao)
}