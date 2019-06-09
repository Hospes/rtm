package ua.hospes.rtm.di.module

import dagger.Module
import dagger.Provides
import ua.hospes.rtm.data.cars.CarsDbStorage
import ua.hospes.rtm.data.cars.CarsRepositoryImpl
import ua.hospes.rtm.data.drivers.DriversDbStorage
import ua.hospes.rtm.data.drivers.DriversRepositoryImpl
import ua.hospes.rtm.data.race.RaceRepositoryImpl
import ua.hospes.rtm.data.sessions.SessionsRepositoryImpl
import ua.hospes.rtm.data.team.TeamsDbStorage
import ua.hospes.rtm.data.team.TeamsRepositoryImpl
import ua.hospes.rtm.domain.cars.CarsRepository
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.sessions.SessionsRepository
import ua.hospes.rtm.domain.team.TeamsRepository
import javax.inject.Singleton

@Module
object RepoModule {

    @Provides @JvmStatic
    internal fun provideRaceRepository(repository: RaceRepositoryImpl): RaceRepository = repository

    @Provides @JvmStatic
    internal fun provideSessionsRepository(repository: SessionsRepositoryImpl): SessionsRepository = repository

    @Singleton
    @Provides @JvmStatic
    internal fun provideDriversRepository(dbStorage: DriversDbStorage, teamsDbStorage: TeamsDbStorage)
            : DriversRepository = DriversRepositoryImpl(dbStorage, teamsDbStorage)

    @Singleton
    @Provides @JvmStatic
    internal fun provideTeamsRepository(db: TeamsDbStorage, driversRepo: DriversRepository)
            : TeamsRepository = TeamsRepositoryImpl(db, driversRepo)

    @Singleton
    @Provides @JvmStatic
    internal fun provideCarsRepository(db: CarsDbStorage)
            : CarsRepository = CarsRepositoryImpl(db)
}