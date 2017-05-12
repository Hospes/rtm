package ua.hospes.rtm.data.team;

import com.google.common.primitives.Ints;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
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
    private final TeamsDbStorage dbStorage;
    private final DriversRepository driversRepository;


    @Inject
    public TeamsRepositoryImpl(TeamsDbStorage dbStorage, DriversRepositoryImpl driversRepository) {
        this.dbStorage = dbStorage;
        this.driversRepository = driversRepository;
    }


    @Override
    public Observable<Team> get() {
        return dbStorage.get()
                .flatMap(teamDb -> Observable.zip(Observable.just(teamDb), getDriverByTeamId(teamDb.getId()).toList(), TeamsMapper::map));
    }

    @Override
    public Observable<Team> get(int id) {
        return dbStorage.get(id)
                .flatMap(teamDb -> Observable.zip(Observable.just(teamDb), getDriverByTeamId(teamDb.getId()).toList(), TeamsMapper::map));
    }

    @Override
    public Observable<Team> getNotInRace() {
        return dbStorage.getNotInRace()
                .flatMap(teamDb -> Observable.zip(Observable.just(teamDb), getDriverByTeamId(teamDb.getId()).toList(), TeamsMapper::map));
    }

    @Override
    public Observable<List<Team>> listen() {
        return dbStorage.listen()
                .flatMap(teamDbs -> Observable.from(teamDbs)
                        .flatMap(teamDb -> Observable.zip(
                                Observable.just(teamDb),
                                getDriverByTeamId(teamDb.getId()).toList(),
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
    public Observable<Boolean> delete(Team team) {
        return dbStorage.remove(TeamsMapper.map(team)).map(count -> count != 0);
    }

    @Override
    public Observable<Void> clear() {
        return dbStorage.clear();
    }


    private Observable<Driver> getDriverByTeamId(int teamId) {
        return driversRepository.getByTeamId(teamId);
    }

    private Observable<Boolean> updateDriversTeam(int teamId, final List<Driver> drivers) {
        return Observable.from(drivers).map(Driver::getId)
                .toList()
                .flatMap(driverIds -> removeDriversFromTeamZip(driversRepository.removeDriversFromTeam(teamId), driverIds))
                .flatMap(driverIds -> driversRepository.addDriversToTeam(teamId, Ints.toArray(driverIds)));
    }

    private Observable<List<Integer>> removeDriversFromTeamZip(Observable<Boolean> driversOperations, List<Integer> driverIds) {
        return Observable.zip(driversOperations, Observable.just(driverIds), (aBoolean, ids) -> ids);
    }
}