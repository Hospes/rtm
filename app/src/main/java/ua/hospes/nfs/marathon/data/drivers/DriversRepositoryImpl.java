package ua.hospes.nfs.marathon.data.drivers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import ua.hospes.nfs.marathon.data.drivers.mapper.DriversMapper;
import ua.hospes.nfs.marathon.data.drivers.storage.DriversDbStorage;
import ua.hospes.nfs.marathon.data.team.models.TeamDb;
import ua.hospes.nfs.marathon.data.team.storage.TeamsDbStorage;
import ua.hospes.nfs.marathon.domain.drivers.DriversRepository;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;

/**
 * @author Andrew Khloponin
 */
@Singleton
public class DriversRepositoryImpl implements DriversRepository {
    private final DriversDbStorage dbStorage;
    private final TeamsDbStorage teamsDbStorage;


    @Inject
    public DriversRepositoryImpl(DriversDbStorage dbStorage, TeamsDbStorage teamsDbStorage) {
        this.dbStorage = dbStorage;
        this.teamsDbStorage = teamsDbStorage;
    }


    @Override
    public Observable<Driver> get() {
        return dbStorage.get()
                .flatMap(driverDb -> Observable.zip(Observable.just(driverDb), getTeamById(driverDb.getTeamId()), DriversMapper::map));
    }

    @Override
    public Observable<Driver> get(int... ids) {
        return dbStorage.get(ids)
                .flatMap(driverDb -> Observable.zip(Observable.just(driverDb), getTeamById(driverDb.getTeamId()), DriversMapper::map));
    }

    @Override
    public Observable<Driver> getByTeamId(int teamId) {
        return dbStorage.getTeamById(teamId)
                .flatMap(driverDb -> Observable.zip(Observable.just(driverDb), getTeamById(driverDb.getTeamId()), DriversMapper::map));
    }

    @Override
    public Observable<List<Driver>> listen() {
        return dbStorage.listen()
                .flatMap(driverDbs -> Observable.from(driverDbs)
                        .flatMap(driverDb -> Observable.zip(Observable.just(driverDb), getTeamById(driverDb.getTeamId()), DriversMapper::map))
                        .toList());
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
    public Observable<Boolean> delete(Driver driver) {
        return dbStorage.remove(DriversMapper.map(driver)).map(count -> count != 0);
    }

    @Override
    public Observable<Void> clear() {
        return dbStorage.clear();
    }


    private Observable<TeamDb> getTeamById(int id) {
        return teamsDbStorage.get().singleOrDefault(null, team -> id == team.getId());
    }
}