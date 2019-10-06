package ua.hospes.rtm.db.cars

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CarDAO {
    @Query("SELECT * FROM cars")
    suspend fun get(): List<CarEntity>

    @Query("SELECT * FROM cars WHERE id = :id LIMIT 1")
    suspend fun get(id: Long): CarEntity

    @Query("SELECT * FROM cars WHERE id IN (:ids)")
    suspend fun getByIds(vararg ids: Long): List<CarEntity>

    @Query("SELECT * FROM cars WHERE id IN (SELECT car_id FROM sessions WHERE end_duration_time IS NULL)")
    suspend fun getNotSelected(): List<CarEntity>


    @Query("SELECT * FROM cars")
    fun observe(): Flow<List<CarEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: CarEntity): Long

    @Query("DELETE FROM cars WHERE id IN (:ids)")
    suspend fun delete(vararg ids: Long)

    @Delete
    suspend fun delete(vararg entities: CarEntity)

    @Query("DELETE FROM cars")
    suspend fun clear()
}