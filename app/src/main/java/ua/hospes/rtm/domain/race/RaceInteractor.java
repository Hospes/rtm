package ua.hospes.rtm.domain.race;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.common.collect.Collections2;
import com.google.common.primitives.Ints;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.rtm.core.db.tables.Race;
import ua.hospes.rtm.domain.cars.CarsRepository;
import ua.hospes.rtm.domain.cars.models.Car;
import ua.hospes.rtm.domain.drivers.DriversRepository;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.domain.preferences.PreferencesManager;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.sessions.SessionsRepository;
import ua.hospes.rtm.domain.sessions.models.Session;
import ua.hospes.rtm.domain.team.TeamsRepository;
import ua.hospes.rtm.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public class RaceInteractor {
    private final PreferencesManager preferencesManager;
    private final RaceRepository raceRepository;
    private final TeamsRepository teamsRepository;
    private final SessionsRepository sessionsRepository;
    private final DriversRepository driversRepository;
    private final CarsRepository carsRepository;


    @Inject
    public RaceInteractor(PreferencesManager preferencesManager, RaceRepository raceRepository, TeamsRepository teamsRepository, SessionsRepository sessionsRepository, DriversRepository driversRepository, CarsRepository carsRepository) {
        this.preferencesManager = preferencesManager;
        this.raceRepository = raceRepository;
        this.teamsRepository = teamsRepository;
        this.sessionsRepository = sessionsRepository;
        this.driversRepository = driversRepository;
        this.carsRepository = carsRepository;
    }


    public Observable<List<RaceItem>> listen() {
        return raceRepository.listen();
    }

    public Observable<List<Team>> getNotInRace() {
        return teamsRepository.getNotInRace().toList();
    }

    public Observable<Boolean> initSession(Session.Type type, int... teamIds) {
        return sessionsRepository.newSessions(type, teamIds)
                .map(session -> {
                    ContentValues cv = new ContentValues();
                    cv.put(Race.SESSION_ID, session.getId());
                    return new Pair<>(session.getTeamId(), cv);
                })
                .toList()
                .flatMap(raceRepository::updateByTeamId);
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

    public Observable<Boolean> startRace(long startTime) {
        return raceRepository.get()
                .filter(item -> item.getSession() != null)
                .map(item -> item.getSession().getId())
                .toList().map(Ints::toArray)
                .flatMap(sessionIds -> Observable.zip(
                        sessionsRepository.startSessions(startTime, sessionIds).toList().singleOrDefault(null),
                        sessionsRepository.setRaceStartTime(startTime, sessionIds).toList().singleOrDefault(null),
                        (sessions, sessions2) -> true)
                );
    }

    public Observable<Boolean> stopRace(long stopTime) {
        return raceRepository.get()
                .filter(item -> item.getSession() != null)
                .map(item -> item.getSession().getId())
                .toList()
                .flatMap(teamIds -> sessionsRepository.closeSessions(stopTime, Ints.toArray(teamIds)))
                .toList()
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
                .flatMap(raceRepository::update);
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
                .flatMap(raceRepository::update);
    }

    public Observable<Driver> getDrivers(int teamId) {
        return driversRepository.getByTeamId(teamId);
    }

    public Observable<Car> getCarsNotInRace() {
        return carsRepository.getNotInRace();
    }

    public Observable<Void> resetRace() {
        return sessionsRepository.clean()
                .flatMap(aVoid -> raceRepository.get())
                .toList()
                .flatMap(raceItems ->
                        Observable.zip(
                                Observable.from(raceItems),
                                sessionsRepository.newSessions(Session.Type.TRACK, Ints.toArray(Collections2.transform(raceItems, input -> input.getTeam().getId()))),
                                (item, session) -> {
                                    item.setSession(session);
                                    return item;
                                }))
                .toList()
                .flatMap(raceRepository::update)
                .map(list -> null);
    }

    public Observable<Void> clear() {
        return Observable.zip(raceRepository.clean(), sessionsRepository.clean(), (aVoid, aVoid2) -> null);
    }


    private Observable<Session> closeSession(long time, Session session) {
        return session == null ? Observable.empty() : sessionsRepository.closeSessions(time, session.getId());
    }

    private Observable<Session> setDriverForLatestPitSession(int teamId, int driverId) {
        return sessionsRepository.getByTeamId(teamId)
                .last(session -> session.getType() == Session.Type.PIT)
                .flatMap(session -> sessionsRepository.setSessionDriver(session.getId(), driverId));
    }
}