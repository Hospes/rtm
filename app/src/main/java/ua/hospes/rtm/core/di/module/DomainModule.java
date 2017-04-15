package ua.hospes.rtm.core.di.module;

import dagger.Module;
import dagger.Provides;
import ua.hospes.rtm.data.cars.CarsRepositoryImpl;
import ua.hospes.rtm.data.drivers.DriversRepositoryImpl;
import ua.hospes.rtm.data.race.RaceRepositoryImpl;
import ua.hospes.rtm.data.sessions.SessionsRepositoryImpl;
import ua.hospes.rtm.data.team.TeamsRepositoryImpl;
import ua.hospes.rtm.domain.cars.CarsRepository;
import ua.hospes.rtm.domain.drivers.DriversRepository;
import ua.hospes.rtm.domain.race.RaceRepository;
import ua.hospes.rtm.domain.sessions.SessionsRepository;
import ua.hospes.rtm.domain.team.TeamsRepository;

/**
 * @author Andrew Khloponin
 */
@Module(includes = {DbModule.class})
public class DomainModule {
    @Provides
    public DriversRepository provideDriversRepository(DriversRepositoryImpl repository) {
        return repository;
    }

    @Provides
    public TeamsRepository provideTeamsRepository(TeamsRepositoryImpl repository) {
        return repository;
    }

    @Provides
    public RaceRepository provideRaceRepository(RaceRepositoryImpl repository) {
        return repository;
    }

    @Provides
    public SessionsRepository provideSessionsRepository(SessionsRepositoryImpl repository) {
        return repository;
    }

    @Provides
    public CarsRepository provideCarsRepository(CarsRepositoryImpl repository) {
        return repository;
    }
}