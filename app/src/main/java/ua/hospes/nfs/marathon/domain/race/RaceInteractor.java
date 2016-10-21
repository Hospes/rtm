package ua.hospes.nfs.marathon.domain.race;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.common.collect.Collections2;
import com.google.common.primitives.Ints;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import ua.hospes.nfs.marathon.core.db.ModelBaseInterface;
import ua.hospes.nfs.marathon.core.db.models.SimpleBaseModel;
import ua.hospes.nfs.marathon.core.db.tables.Race;
import ua.hospes.nfs.marathon.core.di.scope.ActivityScope;
import ua.hospes.nfs.marathon.domain.cars.CarsRepository;
import ua.hospes.nfs.marathon.domain.cars.models.Car;
import ua.hospes.nfs.marathon.domain.drivers.DriversRepository;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.domain.preferences.PreferencesManager;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.domain.sessions.SessionsRepository;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;
import ua.hospes.nfs.marathon.domain.team.TeamsRepository;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
@ActivityScope
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
                .map((Func1<Session, Pair<Integer, ModelBaseInterface>>) session -> {
                    ContentValues cv = new ContentValues();
                    cv.put(Race.SESSION_ID, session.getId());
                    return new Pair<>(session.getTeamId(), new SimpleBaseModel(cv));
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
                .toList()
                .flatMap(sessionIds -> sessionsRepository.startSessions(startTime, Ints.toArray(sessionIds)))
                .toList()
                .map(sessions -> true);
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
        switch (preferencesManager.getPitStopAssign()) {
            case PREVIOUS:
                if (item.getSession() != null && item.getSession().getDriver() != null)
                    driverId = item.getSession().getDriver().getId();
                break;
        }
        return Observable.zip(
                closeSession(time, item.getSession()),
                sessionsRepository.startNewSession(time, Session.Type.PIT, driverId, item.getTeam().getId()),
                (closedSession, openedSession) -> {
                    item.setSession(openedSession);
                    return item;
                })
                .toList()
                .flatMap(raceRepository::update);
    }

    public Observable<Boolean> teamOut(@NonNull RaceItem item, long time) {
        return Observable.zip(
                closeSession(time, item.getSession()),
                sessionsRepository.startNewSessions(time, Session.Type.TRACK, item.getTeam().getId()),
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