package ua.hospes.rtm.data.race;

import android.content.ContentValues;
import android.util.Pair;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Single;
import rx.functions.Func2;
import ua.hospes.rtm.data.race.mapper.RaceMapper;
import ua.hospes.rtm.data.race.operations.UpdateRaceOperation;
import ua.hospes.rtm.data.race.storage.RaceDbStorage;
import ua.hospes.rtm.data.sessions.SessionsRepositoryImpl;
import ua.hospes.rtm.data.sessions.models.SessionDb;
import ua.hospes.rtm.data.sessions.storage.SessionsDbStorage;
import ua.hospes.rtm.data.team.TeamsRepositoryImpl;
import ua.hospes.rtm.domain.race.RaceRepository;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.race.models.RaceItemDetails;
import ua.hospes.rtm.domain.sessions.SessionsRepository;
import ua.hospes.rtm.domain.sessions.models.Session;
import ua.hospes.rtm.domain.team.TeamsRepository;
import ua.hospes.rtm.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
@Singleton
public class RaceRepositoryImpl implements RaceRepository {
    private final RaceDbStorage raceDbStorage;
    private final TeamsRepository teamsRepository;
    private final SessionsDbStorage sessionsDbStorage;
    private final SessionsRepository sessionsRepository;


    @Inject
    public RaceRepositoryImpl(RaceDbStorage raceDbStorage, TeamsRepositoryImpl teamsRepository, SessionsDbStorage sessionsDbStorage, SessionsRepositoryImpl sessionsRepository) {
        this.raceDbStorage = raceDbStorage;
        this.teamsRepository = teamsRepository;
        this.sessionsDbStorage = sessionsDbStorage;
        this.sessionsRepository = sessionsRepository;
    }


    @Override
    public Observable<RaceItem> get() {
        return raceDbStorage.get()
                .flatMap(raceItemDb -> Observable.zip(Observable.just(raceItemDb), getTeamById(raceItemDb.getTeamId()), getSessionById(raceItemDb.getSessionId()), RaceMapper::map))
                .flatMap(raceItem -> Observable.zip(Observable.just(raceItem), getRawSessionsByTeamId(raceItem.getTeam().getId()), new CalculateRaceItemDetails()));
    }

    @Override
    public Observable<List<RaceItem>> listen() {
        return raceDbStorage.listen()
                .flatMap(raceItemDbs -> Observable.from(raceItemDbs)
                        .flatMap(raceItemDb -> Observable.zip(Observable.just(raceItemDb), getTeamById(raceItemDb.getTeamId()), getSessionById(raceItemDb.getSessionId()), RaceMapper::map))
                        .flatMap(raceItem -> Observable.zip(Observable.just(raceItem), getRawSessionsByTeamId(raceItem.getTeam().getId()), new CalculateRaceItemDetails()))
                        .toList());
    }

    @Override
    public Observable<Boolean> addNew(RaceItem... items) {
        return Observable.from(items)
                .map(RaceMapper::map)
                .toList()
                .flatMap(raceDbStorage::add)
                .map(result -> result.getResult() > 0);
    }

    @Override
    public Observable<Boolean> update(List<RaceItem> items) {
        return Observable.from(items)
                .map(RaceMapper::map)
                .map(UpdateRaceOperation::new)
                .toList()
                .flatMap(raceDbStorage::updateRaces);
    }

    @Override
    public Observable<Boolean> updateByTeamId(Iterable<Pair<Integer, ContentValues>> items) {
        return Observable.from(items)
                .map(UpdateRaceOperation::new)
                .toList()
                .flatMap(raceDbStorage::updateRaces);
    }

    @Override
    public Single<Integer> remove(RaceItem item) {
        return raceDbStorage.remove(RaceMapper.map(item));
    }


    @Override
    public Observable<Void> reset() {
        return raceDbStorage.reset();
    }

    @Override
    public Single<Integer> removeAll() {
        return raceDbStorage.removeAll();
    }


    private Observable<Team> getTeamById(int id) {
        return teamsRepository.get(id).singleOrDefault(null, team -> id == team.getId());
    }

    private Observable<Session> getSessionById(int id) {
        return sessionsRepository.get(id).singleOrDefault(null, session -> id == session.getId());
    }

    private Observable<List<SessionDb>> getRawSessionsByTeamId(int teamId) {
        return sessionsDbStorage.getByTeamId(teamId).toList();
    }

    private class CalculateRaceItemDetails implements Func2<RaceItem, List<SessionDb>, RaceItem> {
        @Override
        public RaceItem call(RaceItem item, List<SessionDb> sessions) {
            RaceItemDetails details = new RaceItemDetails();
            int pitStops = -1;
            for (SessionDb session : sessions) {
                pitStops += Session.Type.TRACK.name().equals(session.getType()) ? 1 : 0;
                if (session.getStartDurationTime() == -1 || session.getEndDurationTime() == -1) continue;
                if (session.getDriverId() == -1) continue;
                long duration = session.getEndDurationTime() - session.getStartDurationTime();
                details.addDriverDuration(session.getDriverId(), duration);
            }
            details.setPitStops(pitStops);
            item.setDetails(details);
            return item;
        }
    }
}