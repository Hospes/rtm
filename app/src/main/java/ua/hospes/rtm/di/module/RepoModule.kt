package ua.hospes.rtm.di.module

import dagger.Module
import dagger.Provides
import ua.hospes.rtm.data.cars.CarsRepositoryImpl
import ua.hospes.rtm.data.drivers.DriversRepositoryImpl
import ua.hospes.rtm.data.race.RaceRepositoryImpl
import ua.hospes.rtm.data.sessions.SessionsRepositoryImpl
import ua.hospes.rtm.data.team.TeamsRepositoryImpl
import ua.hospes.rtm.domain.cars.CarsRepository
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.sessions.SessionsRepository
import ua.hospes.rtm.domain.team.TeamsRepository

@Module
object RepoModule {
    @Provides @JvmStatic
    internal fun provideDriversRepository(repository: DriversRepositoryImpl): DriversRepository = repository

    @Provides @JvmStatic
    internal fun provideTeamsRepository(repository: TeamsRepositoryImpl): TeamsRepository = repository

    @Provides @JvmStatic
    internal fun provideRaceRepository(repository: RaceRepositoryImpl): RaceRepository = repository

    @Provides @JvmStatic
    internal fun provideSessionsRepository(repository: SessionsRepositoryImpl): SessionsRepository = repository

    @Provides @JvmStatic
    internal fun provideCarsRepository(repository: CarsRepositoryImpl): CarsRepository = repository
}