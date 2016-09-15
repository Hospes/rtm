package ua.hospes.nfs.marathon.data.race;

import android.util.Pair;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;
import ua.hospes.nfs.marathon.core.db.ModelBaseInterface;
import ua.hospes.nfs.marathon.data.race.mapper.RaceMapper;
import ua.hospes.nfs.marathon.data.race.operations.UpdateRaceOperation;
import ua.hospes.nfs.marathon.data.race.storage.RaceDbStorage;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
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
                .flatMap(raceItemDb -> Observable.zip(Observable.just(raceItemDb), getTeamById(raceItemDb.getTeamId()), getSessionById(raceItemDb.getSessionId()), RaceMapper::map))
                .flatMap(raceItem -> Observable.zip(Observable.just(raceItem), getPitStopsCount(raceItem.getTeam().getId()), (item, pits) -> {
                    item.setPitStops(pits);
                    return item;
                }))
                .flatMap(new GetDriverDurationFunc());
    }

    @Override
    public Observable<List<RaceItem>> listen() {
        return raceDbStorage.listen()
                .flatMap(raceItemDbs -> Observable.from(raceItemDbs)
                        .flatMap(raceItemDb -> Observable.zip(Observable.just(raceItemDb), getTeamById(raceItemDb.getTeamId()), getSessionById(raceItemDb.getSessionId()), RaceMapper::map))
                        .flatMap(raceItem -> Observable.zip(Observable.just(raceItem), getPitStopsCount(raceItem.getTeam().getId()), (item, pits) -> {
                            item.setPitStops(pits);
                            return item;
                        }))
                        .flatMap(new GetDriverDurationFunc())
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
    public Observable<Boolean> updateByTeamId(Iterable<Pair<Integer, ModelBaseInterface>> items) {
        return Observable.from(items)
                .map(UpdateRaceOperation::new)
                .toList()
                .flatMap(raceDbStorage::updateRaces);
    }

    @Override
    public Observable<Boolean> delete(RaceItem item) {
        return raceDbStorage.remove(RaceMapper.map(item)).map(integer -> integer > 0);
    }


    @Override
    public Observable<Void> reset() {
        return raceDbStorage.reset();
    }

    @Override
    public Observable<Void> clean() {
        return raceDbStorage.clean();
    }


    private Observable<Team> getTeamById(int id) {
        return teamsRepository.get(id).singleOrDefault(null, team -> id == team.getId());
    }

    private Observable<Session> getSessionById(int id) {
        return sessionsRepository.get(id).singleOrDefault(null, session -> id == session.getId());
    }

    private Observable<Integer> getPitStopsCount(int teamId) {
        return sessionsRepository.getByTeamId(teamId).filter(session -> session.getType() == Session.Type.PIT).count();
    }

    private Observable<Long> getDriverPrevSessionsDuration(int teamId, int driverId) {
        return sessionsRepository.getByTeamIdAndDriverId(teamId, driverId)
                .filter(session -> session.getStartDurationTime() != -1 && session.getEndDurationTime() != -1)
                .map(session -> session.getEndDurationTime() - session.getStartDurationTime())
                .toList()
                .map(longs -> {
                    long result = 0L;
                    for (Long l : longs) result += l;
                    return result;
                });
    }


    private class GetDriverDurationFunc implements Func1<RaceItem, Observable<RaceItem>> {
        @Override
        public Observable<RaceItem> call(RaceItem item) {
            if (item.getSession() == null || item.getSession().getDriver() == null) return Observable.just(item);
            Driver driver = item.getSession().getDriver();
            return getDriverPrevSessionsDuration(driver.getTeamId(), driver.getId())
                    .map(aLong -> {
                        item.setDriverPrevDuration(aLong);
                        return item;
                    });
        }
    }
}