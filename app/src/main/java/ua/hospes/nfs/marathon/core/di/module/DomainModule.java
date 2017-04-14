package ua.hospes.nfs.marathon.core.di.module;

import dagger.Module;
import dagger.Provides;
import ua.hospes.nfs.marathon.data.cars.CarsRepositoryImpl;
import ua.hospes.nfs.marathon.data.drivers.DriversRepositoryImpl;
import ua.hospes.nfs.marathon.data.race.RaceRepositoryImpl;
import ua.hospes.nfs.marathon.data.sessions.SessionsRepositoryImpl;
import ua.hospes.nfs.marathon.data.team.TeamsRepositoryImpl;
import ua.hospes.nfs.marathon.domain.cars.CarsRepository;
import ua.hospes.nfs.marathon.domain.drivers.DriversRepository;
import ua.hospes.nfs.marathon.domain.race.RaceRepository;
import ua.hospes.nfs.marathon.domain.sessions.SessionsRepository;
import ua.hospes.nfs.marathon.domain.team.TeamsRepository;

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