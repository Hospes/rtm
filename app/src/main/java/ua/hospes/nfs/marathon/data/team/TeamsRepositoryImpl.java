package ua.hospes.nfs.marathon.data.team;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import ua.hospes.nfs.marathon.data.team.mapper.TeamsMapper;
import ua.hospes.nfs.marathon.data.team.storage.TeamsDbStorage;
import ua.hospes.nfs.marathon.domain.team.TeamsRepository;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
@Singleton
public class TeamsRepositoryImpl implements TeamsRepository {
    private final TeamsDbStorage dbStorage;


    @Inject
    public TeamsRepositoryImpl(TeamsDbStorage dbStorage) {
        this.dbStorage = dbStorage;
    }


    @Override
    public Observable<Team> get() {
        return dbStorage.get().map(TeamsMapper::map);
    }

    @Override
    public Observable<List<Team>> listen() {
        return dbStorage.listen().map(TeamsMapper::map);
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
}