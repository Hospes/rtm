package ua.hospes.rtm.data.db.team

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDAO {
    @Query("SELECT * FROM teams")
    suspend fun get(): List<TeamEntity>

    @Query("SELECT * FROM teams WHERE id = :id LIMIT 1")
    suspend fun get(id: Long): TeamEntity

    @Query("SELECT * FROM teams WHERE id IN (:ids)")
    suspend fun get(vararg ids: Long): List<TeamEntity>

    @Query("SELECT t1.* FROM teams t1 LEFT JOIN race t2 ON t2.team_id = t1.id WHERE t2.team_id IS NULL")
    suspend fun getNotInRace(): List<TeamEntity>


    @Query("SELECT * FROM teams")
    fun observe(): Flow<List<TeamEntity>>


    @Transaction
    suspend fun save(entity: TeamEntity, driverIds: LongArray? = null) {
        val id = save(entity)
        driverIds?.let {
            removeDriversFromTeam(id)
            setTeamDrivers(id, it)
        }
    }


    /**
     * Instead of this method you have to use [save]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: TeamEntity): Long

    @Query("DELETE FROM teams WHERE id IN (:ids)")
    suspend fun delete(vararg ids: Long)

    @Delete
    suspend fun delete(vararg entities: TeamEntity)

    @Query("DELETE FROM teams")
    suspend fun clear()


    @Query("UPDATE drivers SET team_id = :teamId WHERE id IN (:driverIds)")
    suspend fun setTeamDrivers(teamId: Long, driverIds: LongArray)

    @Query("UPDATE drivers SET team_id = null WHERE team_id = :teamId")
    suspend fun removeDriversFromTeam(teamId: Long)
}