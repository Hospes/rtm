package ua.hospes.nfs.marathon.data.race;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import rx.Observable;
import ua.hospes.nfs.marathon.data.race.mapper.RaceMapper;
import ua.hospes.nfs.marathon.data.race.storage.RaceDbStorage;
import ua.hospes.nfs.marathon.domain.race.RaceRepository;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.domain.sessions.SessionsRepository;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;
import ua.hospes.nfs.marathon.domain.team.TeamsRepository;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
@Singleton
public class RaceRepositoryImpl implements RaceRepository {
    private final RaceDbStorage raceDbStorage;
    private final TeamsRepository teamsRepository;
    private final SessionsRepository sessionsRepository;


    @Inject
    public RaceRepositoryImpl(RaceDbStorage raceDbStorage, TeamsRepository teamsRepository, SessionsRepository sessionsRepository) {
        this.raceDbStorage = raceDbStorage;
        this.teamsRepository = teamsRepository;
        this.sessionsRepository = sessionsRepository;
    }


    @Override
    public Observable<RaceItem> get() {
        return raceDbStorage.get()
                .flatMap(raceItemDb -> Observable.zip(Observable.just(raceItemDb), getTeamById(raceItemDb.getTeamId()), RaceMapper::map));
    }

    @Override
    public Observable<List<RaceItem>> listen() {
        return raceDbStorage.listen()
                .flatMap(raceItemDbs -> Observable.from(raceItemDbs)
                        .flatMap(raceItemDb -> Observable.zip(Observable.just(raceItemDb), getTeamById(raceItemDb.getTeamId()), RaceMapper::map))
                        .toList());
    }

    @DebugLog
    @Override
    public Observable<Boolean> addNew(RaceItem... items) {
        return Observable.from(items)
                .map(RaceMapper::map)
                .toList()
                .flatMap(raceDbStorage::add)
                .map(result -> result.getResult() > 0);
    }

    @Override
    public Observable<Boolean> delete(RaceItem item) {
        return raceDbStorage.remove(RaceMapper.map(item)).map(integer -> integer > 0);
    }


    private Observable<Team> getTeamById(int id) {
        return teamsRepository.get().takeFirst(team -> id == team.getId());
    }

    private Observable<Session> getSessionById(int id) {
        return sessionsRepository.get().takeFirst(session -> id == session.getId());
    }
}