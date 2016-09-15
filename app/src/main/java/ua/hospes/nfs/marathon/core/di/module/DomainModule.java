package ua.hospes.nfs.marathon.core.di.module;

import javax.inject.Singleton;

import autodagger.AutoExpose;
import dagger.Module;
import dagger.Provides;
import ua.hospes.nfs.marathon.App;
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
@Module
public class DomainModule {
    @Singleton
    @Provides
    @AutoExpose(App.class)
    public DriversRepository provideDriversRepository(DriversRepositoryImpl repository) {
        return repository;
    }

    @Singleton
    @Provides
    @AutoExpose(App.class)
    public TeamsRepository provideTeamsRepository(TeamsRepositoryImpl repository) {
        return repository;
    }

    @Singleton
    @Provides
    @AutoExpose(App.class)
    public RaceRepository provideRaceRepository(RaceRepositoryImpl repository) {
        return repository;
    }

    @Singleton
    @Provides
    @AutoExpose(App.class)
    public SessionsRepository provideSessionsRepository(SessionsRepositoryImpl repository) {
        return repository;
    }

    @Singleton
    @Provides
    @AutoExpose(App.class)
    public CarsRepository provideCarsRepository(CarsRepositoryImpl repository) {
        return repository;
    }

//    @Singleton
//    @Provides
//    @AutoExpose(App.class)
//    public UserRepository provideUserRepository(UserRepositoryImpl repository) {
//        return repository;
//    }
//
//    @Singleton
//    @Provides
//    @AutoExpose(App.class)
//    public GameStatisticRepository provideGameStatisticRepository(GameStatisticRepositoryImpl repository) {
//        return repository;
//    }
//
//    @Singleton
//    @Provides
//    @AutoExpose(App.class)
//    public TeamStatisticRepository provideTeamStatisticRepository(TeamStatisticRepositoryImpl repository) {
//        return repository;
//    }
//
//    @Singleton
//    @Provides
//    @AutoExpose(App.class)
//    public SeasonStandingsRepository provideSeasonStandingsRepository(SeasonStandingsRepositoryImpl repository) {
//        return repository;
//    }
}