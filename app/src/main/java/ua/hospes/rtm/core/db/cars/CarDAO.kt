package ua.hospes.rtm.core.db.cars

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CarDAO {
    @Query("SELECT * FROM cars")
    fun observe(): Flow<List<CarEntity>>

    @Query("SELECT * FROM cars")
    suspend fun get(): List<CarEntity>

    @Query("SELECT * FROM cars WHERE uid IN (:ids)")
    suspend fun getByIds(ids: IntArray): List<CarEntity>

    //@Query("SELECT * FROM cars WHERE uid NOT IN (SELECT car_id FROM sessions WHERE end_time = -1)")
    //SELECT c.* FROM Cars AS c WHERE c._id NOT IN ( SELECT s.car_id FROM Sessions AS s WHERE s.end_time = -1 )
    //suspend fun getNotInRace(): List<CarEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: CarEntity)

    @Query("DELETE FROM cars WHERE uid IN (:ids)")
    suspend fun delete(ids: IntArray)

    @Delete
    suspend fun delete(vararg entities: CarEntity)

    @Query("DELETE FROM cars")
    suspend fun clear()
}