package ua.hospes.rtm.db.team

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TeamDAO {

    @Query("SELECT * FROM teams")
    suspend fun get(): List<TeamEntity>

    @Query("SELECT * FROM teams WHERE id = :id LIMIT 1")
    suspend fun get(id: Int): TeamEntity

    @Query("SELECT * FROM teams WHERE id IN (:ids)")
    suspend fun get(ids: IntArray): List<TeamEntity>

    @Query("SELECT t1.* FROM teams t1 LEFT JOIN race t2 ON t2.team_id = t1.id WHERE t2.team_id IS NULL")
    suspend fun getNotInRace(): List<TeamEntity>

    //    fun getNotInRace(): Observable<TeamEntity> =
    //            dbHelper.querySingle(Function { TeamsMapper.map(it) }, "SELECT t1.rowid, t1.* FROM " + Teams.name +
    //                    " t1 LEFT JOIN " + Race.name + " t2 ON t2." + Race.TEAM_ID.name() + " = t1." + Teams.ID.name() +
    //                    " WHERE t2." + Race.TEAM_ID.name() + " IS NULL")


    @Query("SELECT * FROM teams")
    fun observe(): Flow<List<TeamEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: TeamEntity)

    @Query("DELETE FROM teams WHERE id IN (:ids)")
    suspend fun delete(ids: IntArray)

    @Delete
    suspend fun delete(vararg entities: TeamEntity)

    @Query("DELETE FROM teams")
    suspend fun clear()
}