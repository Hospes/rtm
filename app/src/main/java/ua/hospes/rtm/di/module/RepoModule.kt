package ua.hospes.rtm.di.module

import dagger.Module
import dagger.Provides
import ua.hospes.rtm.core.db.cars.CarDAO
import ua.hospes.rtm.core.db.drivers.DriverDAO
import ua.hospes.rtm.core.db.race.storage.RaceDbStorage
import ua.hospes.rtm.core.db.sessions.storage.SessionsDbStorage
import ua.hospes.rtm.core.db.team.TeamDAO
import ua.hospes.rtm.data.*
import ua.hospes.rtm.domain.cars.CarsRepository
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.sessions.SessionsRepository
import ua.hospes.rtm.domain.team.TeamsRepository
import javax.inject.Singleton

@Module
object RepoModule {
    @Provides @JvmStatic
    internal fun provideRaceRepository(raceDb: RaceDbStorage, teamsRepo: TeamsRepository, sDb: SessionsDbStorage, sRepo: SessionsRepositoryImpl)
            : RaceRepository = RaceRepositoryImpl(raceDb, teamsRepo, sDb, sRepo)

    @Provides @JvmStatic
    internal fun provideSessionsRepository(dbStorage: SessionsDbStorage, driversRepository: DriversRepository, carsRepository: CarsRepository)
            : SessionsRepository = SessionsRepositoryImpl(dbStorage, driversRepository, carsRepository)

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