package ua.hospes.nfs.marathon.data.team;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import ua.hospes.nfs.marathon.data.drivers.DriversRepositoryImpl;
import ua.hospes.nfs.marathon.data.team.mapper.TeamsMapper;
import ua.hospes.nfs.marathon.data.team.storage.TeamsDbStorage;
import ua.hospes.nfs.marathon.domain.drivers.DriversRepository;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.domain.team.TeamsRepository;
import ua.hospes.nfs.marathon.domain.team.models.Team;

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
            return dbStorage.add(TeamsMapper.map(team)).map(result -> result.getResult() != 0);
        } else {
            return dbStorage.update(TeamsMapper.map(team)).map(result -> result.getResult() != 0);
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
}