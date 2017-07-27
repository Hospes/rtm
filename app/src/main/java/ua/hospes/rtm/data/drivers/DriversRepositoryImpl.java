package ua.hospes.rtm.data.drivers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import ua.hospes.rtm.data.drivers.mapper.DriversMapper;
import ua.hospes.rtm.data.drivers.models.DriverDb;
import ua.hospes.rtm.data.drivers.storage.DriversDbStorage;
import ua.hospes.rtm.data.team.models.TeamDb;
import ua.hospes.rtm.data.team.storage.TeamsDbStorage;
import ua.hospes.rtm.domain.drivers.DriversRepository;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.utils.Optional;

/**
 * @author Andrew Khloponin
 */
@Singleton
public class DriversRepositoryImpl implements DriversRepository {
    private final DriversDbStorage dbStorage;
    private final TeamsDbStorage   teamsDbStorage;


    @Inject
    DriversRepositoryImpl(DriversDbStorage dbStorage, TeamsDbStorage teamsDbStorage) {
        this.dbStorage = dbStorage;
        this.teamsDbStorage = teamsDbStorage;
    }


    @Override
    public Observable<Driver> get() {
        return dbStorage.get().flatMap(this::transform);
    }

    @Override
    public Observable<Driver> get(int... ids) {
        return dbStorage.get(ids).flatMap(this::transform);
    }

    @Override
    public Observable<Driver> getByTeamId(int teamId) {
        return dbStorage.getTeamById(teamId).flatMap(this::transform);
    }

    @Override
    public Observable<List<Driver>> listen() {
        return dbStorage.listen().flatMap(this::transform);
    }


    @Override
    public Observable<Boolean> save(Driver driver) {
        if (driver.getId() == -1) {
            return dbStorage.add(DriversMapper.map(driver)).map(result -> result.getResult() != 0);
        } else {
            return dbStorage.update(DriversMapper.map(driver)).map(result -> result.getResult() != 0);
        }
    }

    @Override
    public Single<Integer> remove(int id) {
        return dbStorage.remove(id);
    }

    @Override
    public Observable<Boolean> addDriversToTeam(int teamId, int... driverIds) {
        return dbStorage.addDriversToTeam(teamId, driverIds);
    }

    @Override
    public Observable<Boolean> removeDriversFromTeam(int teamId) {
        return dbStorage.removeDriversFromTeam(teamId);
    }

    @Override
    public Single<Integer> removeAll() {
        return dbStorage.removeAll();
    }


    private Observable<Driver> transform(DriverDb driverDb) {
        return Single.zip(Single.just(driverDb), getTeamById(driverDb.getTeamId()), DriversMapper::map).toObservable();
    }

    private Observable<List<Driver>> transform(List<DriverDb> driverDbs) {
        return Observable.fromIterable(driverDbs)
                .flatMapSingle(driverDb -> Single.zip(Single.just(driverDb), getTeamById(driverDb.getTeamId()), DriversMapper::map))
                .toList().toObservable();
    }

    private Single<Optional<TeamDb>> getTeamById(int id) {
        return teamsDbStorage.get(id).map(Optional::of).single(Optional.empty());
    }
}