package ua.hospes.rtm.db.sessions

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SessionDAO {
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


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: SessionEntity): Long

    @Transaction
    suspend fun save(vararg entities: SessionEntity): LongArray = entities.map { save(it) }.toLongArray()


    @Query("UPDATE sessions SET driver_id = :driverId WHERE id = :sessionId")
    suspend fun setDriver(sessionId: Long, driverId: Long)

    @Query("UPDATE sessions SET car_id = :carId WHERE id = :sessionId")
    suspend fun setCar(sessionId: Long, carId: Long)

    @Query("UPDATE sessions SET race_start_time = :raceStartTime AND start_duration_time = :startTime AND end_duration_time = NULL WHERE id = :sessionId")
    suspend fun openSession(sessionId: Long, raceStartTime: Long, startTime: Long)

    @Query("UPDATE sessions SET end_duration_time = :stopTime WHERE id = :sessionId")
    suspend fun closeSession(sessionId: Long, stopTime: Long)

    @Query("UPDATE sessions SET race_start_time = :startTime WHERE id = :sessionId")
    suspend fun setRaceStartTime(sessionId: Long, startTime: Long)


    /**
     * Request should place start time for all sessions that currently exists in 'race' table
     */
    @Query("UPDATE sessions SET race_start_time = :time, start_duration_time = :time WHERE id IN (SELECT session_id FROM race)")
    suspend fun startRace(time: Long)

    @Query("UPDATE sessions SET end_duration_time = :time WHERE id IN (SELECT session_id FROM race)")
    suspend fun stopRace(time: Long)


    @Query("DELETE FROM sessions WHERE id IN (:ids)")
    suspend fun delete(vararg ids: Long)

    @Delete
    suspend fun delete(vararg entities: SessionEntity)

    @Query("DELETE FROM sessions")
    suspend fun clear()
}