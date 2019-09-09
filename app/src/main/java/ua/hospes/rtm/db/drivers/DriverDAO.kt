package ua.hospes.rtm.db.drivers

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
internal interface DriverDAO {

    @Query("SELECT * FROM drivers")
    suspend fun get(): List<DriverEntity>

    @Query("SELECT * FROM drivers WHERE id IN (:ids)")
    suspend fun getByIds(ids: IntArray): List<DriverEntity>

    @Query("SELECT * FROM drivers WHERE team_id = :id")
    suspend fun getByTeamId(id: Int): List<DriverEntity>


    @Query("SELECT * FROM drivers")
    fun observe(): Flow<List<DriverEntity>>


    @Query("UPDATE drivers SET team_id = :teamId WHERE team_id IN (:driverIds)")
    suspend fun addDriversToTeam(teamId: Int, driverIds: IntArray)

    @Query("UPDATE drivers SET team_id = null WHERE team_id = :teamId")
    suspend fun removeDrviersFromTeam(teamId: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: DriverEntity)

    @Query("DELETE FROM drivers WHERE id IN (:ids)")
    suspend fun delete(ids: IntArray)

    @Delete
    suspend fun delete(vararg entities: DriverEntity)

    @Query("DELETE FROM drivers")
    suspend fun clear()
}