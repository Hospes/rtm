package ua.hospes.rtm.core.db.team

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TeamDAO {

    @Query("SELECT * FROM teams")
    suspend fun get(): List<TeamEntity>

    @Query("SELECT * FROM teams WHERE id IN (:ids)")
    suspend fun getByIds(ids: IntArray): List<TeamEntity>

    //@Query("SELECT * FROM cars WHERE uid NOT IN (SELECT car_id FROM sessions WHERE end_time = -1)")
    //SELECT c.* FROM Cars AS c WHERE c._id NOT IN ( SELECT s.car_id FROM Sessions AS s WHERE s.end_time = -1 )
    //suspend fun getNotInRace(): List<CarEntity>

    //    fun getNotInRace(): Observable<TeamEntity> =
    //            dbHelper.querySingle(Function { TeamsMapper.map(it) }, "SELECT t1.rowid, t1.* FROM " + Teams.name +
    //                    " t1 LEFT JOIN " + Race.name + " t2 ON t2." + Race.TEAM_ID.name() + " = t1." + Teams.ID.name() +
    //                    " WHERE t2." + Race.TEAM_ID.name() + " IS NULL")


    @Query("SELECT * FROM teams")
    fun observe(): Flow<List<TeamEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: TeamEntity)

    @Query("DELETE FROM teams WHERE id IN (:ids)")
    suspend fun delete(ids: IntArray)

    @Delete
    suspend fun delete(vararg entities: TeamEntity)

    @Query("DELETE FROM teams")
    suspend fun clear()
}