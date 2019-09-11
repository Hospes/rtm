package ua.hospes.rtm.domain.sessions

import kotlinx.coroutines.flow.Flow

internal interface SessionsRepository {
    suspend fun get(): List<Session>

    suspend fun get(vararg ids: Int): List<Session>

    suspend fun getByTeam(teamId: Int): List<Session>

    suspend fun getByTeamAndDriver(teamId: Int, driverId: Int): List<Session>


    fun listen(): Flow<List<Session>>

    fun listenByTeamId(teamId: Int): Flow<List<Session>>


    suspend fun setSessionDriver(sessionId: Int, driverId: Int)

    suspend fun setSessionCar(sessionId: Int, carId: Int)


    suspend fun newSessions(type: Session.Type, vararg teamIds: Int)

    suspend fun startSessions(raceStartTime: Long, startTime: Long, vararg sessionIds: Int)

    suspend fun startNewSessions(raceStartTime: Long, startTime: Long, type: Session.Type, vararg teamIds: Int)

    /**
     * Create new session in [Sessions] table with
     *
     * @param startTime start time in nanoseconds
     * @param type      session type [Session.Type]
     * @param driverId  predefined session driver or -1 if no driver
     * @param teamId    team id
     */
    suspend fun startNewSession(raceStartTime: Long, startTime: Long, type: Session.Type, driverId: Int, teamId: Int)

    /**
     * Close list of sessions by ids
     *
     * @param stopTime   Stop time in nanoseconds
     * @param sessionIds Array of sessions that should be closed
     */
    suspend fun closeSessions(stopTime: Long, vararg sessionIds: Int)

    suspend fun removeLastSession(teamId: Int)


    suspend fun clear()
}