package ua.hospes.rtm.domain.sessions

import kotlinx.coroutines.flow.Flow

internal interface SessionsRepository {
    suspend fun get(): List<Session>

    suspend fun get(vararg ids: Long): List<Session>

    suspend fun getByTeam(teamId: Long): List<Session>

    suspend fun getByTeamAndDriver(teamId: Long, driverId: Long): List<Session>


    fun listen(): Flow<List<Session>>

    fun listenByTeamId(teamId: Long): Flow<List<Session>>

    fun listenByRaceId(raceId: Long): Flow<List<Session>>


    suspend fun setSessionDriver(sessionId: Long, driverId: Long)

    suspend fun setSessionCar(sessionId: Long, carId: Long)


    suspend fun startRace(time: Long)
    suspend fun stopRace(time: Long)
    suspend fun resetRace()


    suspend fun newSession(type: Session.Type, teamId: Long): Session
    suspend fun newSessions(type: Session.Type, vararg teamIds: Long)

    suspend fun startSessions(raceStartTime: Long, startTime: Long, vararg sessionIds: Long)

    suspend fun startNewSessions(raceStartTime: Long, startTime: Long, type: Session.Type, vararg teamIds: Long)

    /**
     * Create new session in [Sessions] table with
     *
     * @param startTime start time in nanoseconds
     * @param type      session type [Session.Type]
     * @param driverId  predefined session driver or -1 if no driver
     * @param teamId    team id
     */
    suspend fun startNewSession(teamId: Long,
                                raceStartTime: Long,
                                startTime: Long,
                                type: Session.Type = Session.Type.TRACK,
                                driverId: Long? = null): Session

    /**
     * Close list of sessions by ids
     *
     * @param stopTime   Stop time in nanoseconds
     * @param sessionIds Array of sessions that should be closed
     */
    suspend fun closeSessions(stopTime: Long, vararg sessionIds: Long)

    suspend fun removeLastSession(teamId: Long)


    suspend fun clear()
}