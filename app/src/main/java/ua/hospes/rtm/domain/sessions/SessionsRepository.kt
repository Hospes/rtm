package ua.hospes.rtm.domain.sessions

import kotlinx.coroutines.flow.Flow

internal interface SessionsRepository {
    suspend fun get(): List<Session>
    suspend fun getByTeam(teamId: Long): List<Session>

    fun listenByRaceId(raceId: Long): Flow<List<Session>>

    suspend fun setSessionDriver(sessionId: Long, driverId: Long)
    suspend fun setSessionCar(sessionId: Long, carId: Long)

    suspend fun startRace(time: Long)
    suspend fun stopRace(time: Long)
    suspend fun resetRace()

    suspend fun newSession(type: Session.Type, teamId: Long): Session
    suspend fun closeCurrentStartNew(raceItemId: Long, currentTime: Long, type: Session.Type)
    suspend fun removeLastSession(teamId: Long)

    suspend fun clear()
}