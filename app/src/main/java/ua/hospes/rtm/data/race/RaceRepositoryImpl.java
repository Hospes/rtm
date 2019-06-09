package ua.hospes.rtm.data.race;

import android.content.ContentValues;
import android.util.Pair;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import ua.hospes.rtm.data.race.mapper.RaceMapper;
import ua.hospes.rtm.data.race.operations.UpdateRaceOperation;
import ua.hospes.rtm.data.race.storage.RaceDbStorage;
import ua.hospes.rtm.data.sessions.SessionsRepositoryImpl;
import ua.hospes.rtm.data.sessions.models.SessionDb;
import ua.hospes.rtm.data.sessions.storage.SessionsDbStorage;
import ua.hospes.rtm.domain.race.RaceRepository;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.race.models.RaceItemDetails;
import ua.hospes.rtm.domain.sessions.Session;
import ua.hospes.rtm.domain.sessions.SessionsRepository;
import ua.hospes.rtm.domain.team.Team;
import ua.hospes.rtm.domain.team.TeamsRepository;
import ua.hospes.rtm.utils.Optional;

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
    RaceRepositoryImpl(RaceDbStorage raceDbStorage, TeamsRepository teamsRepository, SessionsDbStorage sessionsDbStorage, SessionsRepositoryImpl sessionsRepository) {
        this.raceDbStorage = raceDbStorage;
        this.teamsRepository = teamsRepository;
        this.sessionsDbStorage = sessionsDbStorage;
        this.sessionsRepository = sessionsRepository;
    }


    @Override
    public Observable<RaceItem> get() {
        return raceDbStorage.get()
                .flatMapSingle(raceItemDb -> Single.zip(Single.just(raceItemDb), getTeamById(raceItemDb.getTeamId()), getSessionById(raceItemDb.getSessionId()), RaceMapper::map))
                .flatMapSingle(raceItem -> Single.zip(Single.just(raceItem), getRawSessionsByTeamId(raceItem.getTeam().getId()), new CalculateRaceItemDetails()));
    }

    @Override
    public Observable<List<RaceItem>> listen() {
        return raceDbStorage.listen()
                .flatMap(raceItemDbs -> Observable.fromIterable(raceItemDbs)
                        .flatMapSingle(raceItemDb -> Single.zip(Single.just(raceItemDb), getTeamById(raceItemDb.getTeamId()), getSessionById(raceItemDb.getSessionId()), RaceMapper::map))
                        .flatMapSingle(raceItem -> Single.zip(Single.just(raceItem), getRawSessionsByTeamId(raceItem.getTeam().getId()), new CalculateRaceItemDetails()))
                        .toList().toObservable()
                );
    }

    @Override
    public Observable<RaceItem> listen(int id) {
        return raceDbStorage.listen(id)
                .flatMap(raceItemDbs -> Observable.fromIterable(raceItemDbs)
                        .flatMapSingle(raceItemDb -> Single.zip(Single.just(raceItemDb), getTeamById(raceItemDb.getTeamId()), getSessionById(raceItemDb.getSessionId()), RaceMapper::map))
                        .flatMapSingle(raceItem -> Single.zip(Single.just(raceItem), getRawSessionsByTeamId(raceItem.getTeam().getId()), new CalculateRaceItemDetails()))
                        .toList().toObservable()
                )
                .flatMapIterable(raceItems -> raceItems);
    }

    @Override
    public Observable<Boolean> addNew(RaceItem... items) {
        return Observable.fromArray(items)
                .map(RaceMapper::map)
                .toList()
                .flatMapObservable(raceDbStorage::add)
                .map(result -> result.getResult() > 0);
    }

    @Override
    public Observable<Boolean> update(List<RaceItem> items) {
        return Observable.fromIterable(items)
                .map(RaceMapper::map)
                .map(UpdateRaceOperation::new)
                .toList()
                .flatMapObservable(raceDbStorage::updateRaces);
    }

    @Override
    public Observable<Boolean> updateByTeamId(Iterable<Pair<Integer, ContentValues>> items) {
        return Observable.fromIterable(items)
                .map(UpdateRaceOperation::new)
                .toList()
                .flatMapObservable(raceDbStorage::updateRaces);
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


    private Single<Optional<Team>> getTeamById(int id) {
        return teamsRepository.get(id).map(Optional::of);
    }

    private Single<Optional<Session>> getSessionById(int id) {
        return sessionsRepository.get(id).map(Optional::of).single(Optional.empty());
    }

    private Single<List<SessionDb>> getRawSessionsByTeamId(int teamId) {
        return sessionsDbStorage.getByTeamId(teamId).toList();
    }

    private class CalculateRaceItemDetails implements BiFunction<RaceItem, List<SessionDb>, RaceItem> {
        @Override
        public RaceItem apply(RaceItem item, List<SessionDb> sessions) throws Exception {
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