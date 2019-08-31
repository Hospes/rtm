package ua.hospes.rtm.domain.sessions

import io.reactivex.Observable
import io.reactivex.Single
import ua.hospes.rtm.core.db.tables.Sessions

/**
 * @author Andrew Khloponin
 */
interface SessionsRepository {
    fun get(): Observable<Session>

    operator fun get(vararg ids: Int): Observable<Session>

    fun getByTeamId(teamId: Int): Observable<Session>

    fun getByTeamIdAndDriverId(teamId: Int, driverId: Int): Observable<Session>

    fun listen(): Observable<List<Session>>

    fun listenByTeamId(teamId: Int): Observable<List<Session>>

    fun newSessions(type: Session.Type, vararg teamIds: Int): Observable<Session>

    fun setSessionDriver(sessionId: Int, driverId: Int): Observable<Session>

    fun setSessionCar(sessionId: Int, carId: Int): Observable<Session>

    fun startSessions(raceStartTime: Long, startTime: Long, vararg sessionIds: Int): Observable<Session>

    fun startNewSessions(raceStartTime: Long, startTime: Long, type: Session.Type, vararg teamIds: Int): Observable<Session>

    /**
     * Create new session in [Sessions] table with
     *
     * @param startTime start time in nanoseconds
     * @param type      session type [Session.Type]
     * @param driverId  predefined session driver or -1 if no driver
     * @param teamId    team id
     */
    fun startNewSession(raceStartTime: Long, startTime: Long, type: Session.Type, driverId: Int, teamId: Int): Observable<Session>

    /**
     * Close list of sessions by ids
     *
     * @param stopTime   Stop time in nanoseconds
     * @param sessionIds Array of sessions that should be closed
     */
    fun closeSessions(stopTime: Long, vararg sessionIds: Int): Observable<Session>

    fun removeLastSession(teamId: Int): Observable<Session>

    fun removeAll(): Single<Int>
}