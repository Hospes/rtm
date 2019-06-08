package ua.hospes.rtm.domain.race;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.common.collect.Collections2;
import com.google.common.primitives.Ints;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import jxl.write.WriteException;
import ua.hospes.rtm.domain.cars.Car;
import ua.hospes.rtm.domain.cars.CarsRepository;
import ua.hospes.rtm.domain.drivers.DriversRepository;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.domain.preferences.PreferencesManager;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.sessions.SessionsRepository;
import ua.hospes.rtm.domain.sessions.models.Session;
import ua.hospes.rtm.domain.team.models.Team;
import ua.hospes.rtm.utils.Optional;
import ua.hospes.rtm.utils.XLSTest;

/**
 * @author Andrew Khloponin
 */
public class RaceInteractor {
    private final PreferencesManager preferencesManager;
    private final RaceRepository raceRepository;
    private final SessionsRepository sessionsRepository;
    private final DriversRepository driversRepository;
    private final CarsRepository carsRepository;


    @Inject
    RaceInteractor(PreferencesManager preferencesManager, RaceRepository raceRepository, SessionsRepository sessionsRepository, DriversRepository driversRepository, CarsRepository carsRepository) {
        this.preferencesManager = preferencesManager;
        this.raceRepository = raceRepository;
        this.sessionsRepository = sessionsRepository;
        this.driversRepository = driversRepository;
        this.carsRepository = carsRepository;
    }


    public Observable<List<RaceItem>> listen() {
        return raceRepository.listen();
    }

    public Observable<Boolean> setDriver(int sessionId, int teamId, int driverId) {
        switch (preferencesManager.getPitStopAssign()) {
            case NEXT:
                return Observable.zip(
                        setDriverForLatestPitSession(teamId, driverId),
                        sessionsRepository.setSessionDriver(sessionId, driverId),
                        (session, session2) -> true
                );
            default:
                return sessionsRepository.setSessionDriver(sessionId, driverId).map(session -> true);
        }
    }

    public Observable<Boolean> setCar(int sessionId, Car car) {
        return sessionsRepository.setSessionCar(sessionId, car.getId())
                .map(session -> true);
    }

    public Single<Boolean> startRace(long startTime) {
        return raceRepository.get()
                .filter(item -> item.getSession() != null)
                .map(item -> item.getSession().getId())
                .toList().map(Ints::toArray)
                .flatMap(sessionIds -> sessionsRepository.startSessions(startTime, startTime, sessionIds).toList())
                .map(sessions -> true);
    }

    public Single<Boolean> stopRace(long stopTime) {
        return raceRepository.get()
                .filter(item -> item.getSession() != null)
                .map(item -> item.getSession().getId())
                .toList().map(Ints::toArray)
                .flatMap(teamIds -> sessionsRepository.closeSessions(stopTime, teamIds).toList())
                .map(sessions -> true);
    }

    public Observable<Boolean> teamPit(@NonNull RaceItem item, long time) {
        int driverId = -1;
        long raceStartTime = -1;
        if (item.getSession() != null) raceStartTime = item.getSession().getRaceStartTime();
        switch (preferencesManager.getPitStopAssign()) {
            case PREVIOUS:
                if (item.getSession() != null && item.getSession().getDriver() != null)
                    driverId = item.getSession().getDriver().getId();
                break;
        }
        return Observable.zip(
                closeSession(time, item.getSession()),
                sessionsRepository.startNewSession(raceStartTime, time, Session.Type.PIT, driverId, item.getTeam().getId()),
                (closedSession, openedSession) -> {
                    item.setSession(openedSession);
                    return item;
                })
                .toList()
                .flatMapObservable(raceRepository::update);
    }

    public Observable<Boolean> teamOut(@NonNull RaceItem item, long time) {
        long raceStartTime = -1;
        if (item.getSession() != null) raceStartTime = item.getSession().getRaceStartTime();
        return Observable.zip(
                closeSession(time, item.getSession()),
                sessionsRepository.startNewSessions(raceStartTime, time, Session.Type.TRACK, item.getTeam().getId()),
                (closedSession, openedSession) -> {
                    item.setSession(openedSession);
                    return item;
                })
                .toList()
                .flatMapObservable(raceRepository::update);
    }

    public Observable<Boolean> removeLastSession(@NonNull RaceItem raceItem) {
        return Observable.zip(sessionsRepository.removeLastSession(raceItem.getTeam().getId()), Observable.just(raceItem), this::updateRaceItemSession)
                .toList()
                .flatMapObservable(raceRepository::update);
    }

    private RaceItem updateRaceItemSession(Session session, RaceItem item) {
        item.setSession(session);
        return item;
    }


    public Observable<Driver> getDrivers(int teamId) {
        return driversRepository.getByTeamId(teamId);
    }

    public Observable<Car> getCarsNotInRace() {
        return carsRepository.getNotInRace();
    }

    public Observable<Optional> resetRace() {
        return sessionsRepository.removeAll()
                .flatMap(count -> raceRepository.get().toList())
                .flatMap(raceItems ->
                        Observable.zip(
                                Observable.fromIterable(raceItems),
                                sessionsRepository.newSessions(Session.Type.TRACK, Ints.toArray(Collections2.transform(raceItems, input -> input.getTeam().getId()))),
                                (item, session) -> {
                                    item.setSession(session);
                                    return item;
                                }).toList()
                )
                .flatMapObservable(raceRepository::update)
                .map(list -> Optional.empty());
    }

    public Single<Void> removeAll() {
        return Single.zip(raceRepository.removeAll(), sessionsRepository.removeAll(), (aVoid, aVoid2) -> null);
    }


    public Single<File> exportXLS() {
        return raceRepository.get()
                .map(RaceItem::getTeam)
                .flatMap(team -> Observable.zip(Observable.just(team), getTeamSessions(team.getId()), Pair::new))
                .toList().map(pairs -> {
                    Map<Team, List<Session>> data = new HashMap<>();
                    for (Pair<Team, List<Session>> pair : pairs) {
                        data.put(pair.first, pair.second);
                    }
                    return data;
                })
                .flatMap(teams -> Single.create(subscriber -> {
                    try {
                        subscriber.onSuccess(XLSTest.createWorkbook(teams));
                    } catch (IOException | WriteException e) {
                        subscriber.onError(e);
                    }
                }));
    }

    private Observable<List<Session>> getTeamSessions(int teamId) {
        return sessionsRepository.getByTeamId(teamId).toList().toObservable();
    }


    private Observable<Session> closeSession(long time, Session session) {
        return session == null ? Observable.empty() : sessionsRepository.closeSessions(time, session.getId());
    }

    @SuppressWarnings("ConstantConditions")
    private Observable<Session> setDriverForLatestPitSession(int teamId, int driverId) {
        return sessionsRepository.getByTeamId(teamId)
                .filter(session -> session.getType() == Session.Type.PIT)
                .map(Optional::of).last(Optional.empty())
                .flatMapObservable(session -> session.isPresent() ? sessionsRepository.setSessionDriver(session.get().getId(), driverId) : Observable.empty());
    }
}