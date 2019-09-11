package ua.hospes.rtm.db.race

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
internal interface RaceDAO {

    @Query("SELECT * FROM race")
    suspend fun get(): List<RaceEntity>


    @Query("SELECT * FROM race")
    fun observe(): Flow<List<RaceEntity>>

    @Query("SELECT * FROM race WHERE id = :id LIMIT 1")
    fun observe(id: Int): Flow<RaceEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: RaceEntity): Long


    @Query("UPDATE race SET session_id = NULL")
    suspend fun reset()


    @Query("DELETE FROM race WHERE id IN (:ids)")
    suspend fun delete(vararg ids: Int)

    @Delete
    suspend fun delete(vararg entities: RaceEntity)

    @Query("DELETE FROM teams")
    suspend fun clear()
}