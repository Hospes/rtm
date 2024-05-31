package ua.hospes.rtm.data.db.sessions

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ua.hospes.rtm.data.db.race.RaceEntity

@Dao
interface SessionDAO {
    @Query("SELECT * FROM sessions")
    suspend fun get(): List<SessionEntity>

    @Query("SELECT * FROM sessions WHERE id = :id LIMIT 1")
    suspend fun get(id: Long): SessionEntity

    @Query("SELECT * FROM sessions WHERE id IN (:ids)")
    suspend fun get(vararg ids: Long): List<SessionEntity>

    @Query("SELECT * FROM sessions WHERE team_id = :id")
    suspend fun getByTeam(id: Long): List<SessionEntity>

    @Query("SELECT * FROM sessions WHERE team_id = :teamId AND driver_id = :driverId")
    suspend fun getByTeamAndDriver(teamId: Long, driverId: Long): List<SessionEntity>


    @Query("SELECT * FROM sessions")
    fun observe(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE team_id = :teamId")
    fun observeByTeam(teamId: Long): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE team_id IN (SELECT team_id FROM race WHERE id = :raceId)")
    fun observeByRace(raceId: Long): Flow<List<SessionEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: SessionEntity): Long

    @Transaction
    suspend fun save(vararg entities: SessionEntity): LongArray = entities.map { save(it) }.toLongArray()


    @Query("UPDATE sessions SET driver_id = :driverId WHERE id = :sessionId")
    suspend fun setDriver(sessionId: Long, driverId: Long)

    @Query("UPDATE sessions SET car_id = :carId WHERE id = :sessionId")
    suspend fun setCar(sessionId: Long, carId: Long)

    @Query("UPDATE sessions SET race_start_time = :raceStartTime AND start_time = :startTime AND end_time = NULL WHERE id = :sessionId")
    suspend fun openSession(sessionId: Long, raceStartTime: Long, startTime: Long)

    @Query("UPDATE sessions SET end_time = :stopTime WHERE id = :sessionId")
    suspend fun closeSession(sessionId: Long, stopTime: Long)

    @Query("UPDATE sessions SET race_start_time = :startTime WHERE id = :sessionId")
    suspend fun setRaceStartTime(sessionId: Long, startTime: Long)


    @Transaction
    suspend fun closeCurrentStartNew(raceItemId: Long, time: Long, type: SessionEntity.Type) {
        val item = getRaceItem(raceItemId)

        val raceStartTime = item.sessionId?.let {
            val session = get(it)
            closeSession(it, time)
            session.raceStartTime
        }
        val newSessionId = save(
            SessionEntity(
                teamId = item.teamId,
                raceStartTime = raceStartTime,
                startTime = time,
                type = type
            )
        )
        saveRace(item.copy(sessionId = newSessionId))
    }


    @Transaction
    suspend fun resetRace() {
        clear()
        getRaceItems().forEach {
            val id = save(SessionEntity(teamId = it.teamId, type = SessionEntity.Type.TRACK))
            saveRace(it.copy(sessionId = id))
        }
    }


    @Query("SELECT COUNT() FROM sessions WHERE team_id = :teamId AND type = :type")
    suspend fun teamSessionsCount(teamId: Long, type: String = SessionEntity.Type.TRACK.name): Int


    /**
     * Request should place start time for all sessions that currently exists in 'race' table
     */
    @Query("UPDATE sessions SET race_start_time = :time, start_time = :time WHERE id IN (SELECT session_id FROM race)")
    suspend fun startRace(time: Long)

    @Query("UPDATE sessions SET end_time = :time WHERE id IN (SELECT session_id FROM race)")
    suspend fun stopRace(time: Long)


    @Query("DELETE FROM sessions WHERE id IN (:ids)")
    suspend fun delete(vararg ids: Long)

    @Delete
    suspend fun delete(vararg entities: SessionEntity)

    @Query("DELETE FROM sessions")
    suspend fun clear()

    // Additional methods
    @Query("SELECT * FROM race WHERE id = :id")
    suspend fun getRaceItem(id: Long): RaceEntity

    @Query("SELECT * FROM race")
    suspend fun getRaceItems(): List<RaceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRace(entity: RaceEntity): Long
}