package ua.hospes.nfs.marathon.domain.sessions;

import java.util.List;

import rx.Observable;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;

/**
 * @author Andrew Khloponin
 */
public interface SessionsRepository {
    Observable<Session> get();

    Observable<Session> get(int... ids);

    Observable<Session> getByTeamId(int teamId);

    Observable<Session> getByTeamIdAndDriverId(int teamId, int driverId);

    Observable<List<Session>> listen();

    Observable<List<Session>> listenByTeamId(int teamId);

    Observable<Session> newSessions(Session.Type type, int... teamIds);

    Observable<Session> setSessionDriver(int sessionId, int driverId);

    Observable<Session> setSessionCar(int sessionId, int carId);

    Observable<Session> setRaceStartTime(long raceStartTime, int... sessionIds);

    Observable<Session> startSessions(long startTime, int... sessionIds);

    Observable<Session> startNewSessions(long raceStartTime, long startTime, Session.Type type, int... teamIds);

    /**
     * Create new session in {@link Sessions} table with
     *
     * @param startTime start time in nanoseconds
     * @param type      session type {@link Session.Type}
     * @param driverId  predefined session driver or -1 if no driver
     * @param teamId    team id
     */
    Observable<Session> startNewSession(long raceStartTime, long startTime, Session.Type type, int driverId, int teamId);

    /**
     * Close list of sessions by ids
     *
     * @param stopTime   Stop time in nanoseconds
     * @param sessionIds Array of sessions that should be closed
     */
    Observable<Session> closeSessions(long stopTime, int... sessionIds);

    Observable<Void> clean();
}