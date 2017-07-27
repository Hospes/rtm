package ua.hospes.rtm.data.team;

import com.google.common.primitives.Ints;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import ua.hospes.rtm.data.drivers.DriversRepositoryImpl;
import ua.hospes.rtm.data.team.mapper.TeamsMapper;
import ua.hospes.rtm.data.team.storage.TeamsDbStorage;
import ua.hospes.rtm.domain.drivers.DriversRepository;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.domain.team.TeamsRepository;
import ua.hospes.rtm.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
@Singleton
public class TeamsRepositoryImpl implements TeamsRepository {
    private final TeamsDbStorage    dbStorage;
    private final DriversRepository driversRepository;


    @Inject
    TeamsRepositoryImpl(TeamsDbStorage dbStorage, DriversRepositoryImpl driversRepository) {
        this.dbStorage = dbStorage;
        this.driversRepository = driversRepository;
    }


    @Override
    public Observable<Team> get() {
        return dbStorage.get()
                .flatMapSingle(teamDb -> Single.zip(Single.just(teamDb), getDriversByTeamId(teamDb.getId()), TeamsMapper::map));
    }

    @Override
    public Observable<Team> get(int id) {
        return dbStorage.get(id)
                .flatMapSingle(teamDb -> Single.zip(Single.just(teamDb), getDriversByTeamId(teamDb.getId()), TeamsMapper::map));
    }

    @Override
    public Observable<Team> getNotInRace() {
        return dbStorage.getNotInRace()
                .flatMapSingle(teamDb -> Single.zip(Single.just(teamDb), getDriversByTeamId(teamDb.getId()), TeamsMapper::map));
    }

    @Override
    public Observable<List<Team>> listen() {
        return dbStorage.listen()
                .flatMapSingle(teamDbs -> Observable.fromIterable(teamDbs)
                        .flatMapSingle(teamDb -> Single.zip(
                                Single.just(teamDb),
                                getDriversByTeamId(teamDb.getId()),
                                TeamsMapper::map)).toList()
                );
    }

    @Override
    public Observable<Boolean> save(Team team) {
        if (team.getId() == -1) {
            return dbStorage.add(TeamsMapper.map(team))
                    .flatMap(result -> {
                        int teamId = (int) result.getResult();
                        return updateDriversTeam(teamId, team.getDrivers());
                    });
        } else {
            return dbStorage.update(TeamsMapper.map(team))
                    .flatMap(result -> {
                        int teamId = result.getData().getId();
                        return updateDriversTeam(teamId, team.getDrivers());
                    });
        }
    }

    @Override
    public Single<Integer> remove(int id) {
        return dbStorage.remove(id);
    }

    @Override
    public Single<Integer> removeAll() {
        return dbStorage.removeAll();
    }


    private Single<List<Driver>> getDriversByTeamId(int teamId) {
        return driversRepository.getByTeamId(teamId).toList();
    }

    private Observable<Boolean> updateDriversTeam(int teamId, final List<Driver> drivers) {
        return Observable.fromIterable(drivers).map(Driver::getId)
                .toList()
                .flatMapObservable(driverIds -> removeDriversFromTeamZip(driversRepository.removeDriversFromTeam(teamId), driverIds))
                .flatMap(driverIds -> driversRepository.addDriversToTeam(teamId, Ints.toArray(driverIds)));
    }

    private Observable<List<Integer>> removeDriversFromTeamZip(Observable<Boolean> driversOperations, List<Integer> driverIds) {
        return Observable.zip(driversOperations, Observable.just(driverIds), (aBoolean, ids) -> ids);
    }
}