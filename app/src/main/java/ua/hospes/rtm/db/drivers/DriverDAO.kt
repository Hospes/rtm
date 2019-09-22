package ua.hospes.rtm.db.drivers

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
internal interface DriverDAO {
    @Query("SELECT * FROM drivers")
    suspend fun get(): List<DriverEntity>

    @Query("SELECT * FROM drivers WHERE id = :id LIMIT 1")
    suspend fun get(id: Long): DriverEntity

    @Query("SELECT * FROM drivers WHERE id IN (:ids)")
    suspend fun get(vararg ids: Long): List<DriverEntity>

    @Query("SELECT * FROM drivers WHERE team_id = :id")
    suspend fun getByTeamId(id: Long): List<DriverEntity>

    @Query("SELECT d.* FROM drivers d LEFT JOIN sessions s ON s.driver_id = d.id WHERE s.driver_id IS NULL AND s.end_duration_time IS NULL AND d.team_id = :teamId")
    suspend fun getNotSelected(teamId: Long): List<DriverEntity>


    @Query("SELECT * FROM drivers")
    fun observe(): Flow<List<DriverEntity>>


    @Query("UPDATE drivers SET team_id = :teamId WHERE id IN (:driverIds)")
    suspend fun addDriversToTeam(teamId: Long, driverIds: LongArray)

    @Query("UPDATE drivers SET team_id = null WHERE team_id = :teamId")
    suspend fun removeDriversFromTeam(teamId: Long)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: DriverEntity): Long

    @Query("DELETE FROM drivers WHERE id IN (:ids)")
    suspend fun delete(vararg ids: Long)

    @Delete
    suspend fun delete(vararg entities: DriverEntity)

    @Query("DELETE FROM drivers")
    suspend fun clear()
}