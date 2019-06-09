package ua.hospes.rtm.data.team

import android.database.sqlite.SQLiteDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function
import ua.hospes.dbhelper.InsertResult
import ua.hospes.dbhelper.UpdateResult
import ua.hospes.dbhelper.builder.DeleteQuery
import ua.hospes.dbhelper.builder.SelectQuery
import ua.hospes.dbhelper.builder.UpdateQuery
import ua.hospes.dbhelper.builder.conditions.Condition
import ua.hospes.rtm.core.db.DbHelper
import ua.hospes.rtm.core.db.tables.Drivers
import ua.hospes.rtm.core.db.tables.Race
import ua.hospes.rtm.core.db.tables.Teams
import javax.inject.Inject

class TeamsDbStorage @Inject constructor(private val dbHelper: DbHelper) {

    fun add(Team: TeamDb): Observable<InsertResult<TeamDb>> = dbHelper.insert(Teams.name, SQLiteDatabase.CONFLICT_ABORT, Team)

    fun update(team: TeamDb): Observable<UpdateResult<TeamDb>> =
            dbHelper.update(UpdateQuery(Teams.name).where(Condition.eq(Teams.ID, team.id)), team)


    fun getNotInRace(): Observable<TeamDb> =
            dbHelper.querySingle(Function { TeamsMapper.map(it) }, "SELECT t1.rowid, t1.* FROM " + Teams.name +
                    " t1 LEFT JOIN " + Race.name + " t2 ON t2." + Race.TEAM_ID.name() + " = t1." + Teams.ID.name() +
                    " WHERE t2." + Race.TEAM_ID.name() + " IS NULL")


    fun get(): Observable<TeamDb> =
            dbHelper.querySingle({ TeamsMapper.map(it) }, SelectQuery(Teams.name))

    fun get(id: Int): Observable<TeamDb> =
            dbHelper.querySingle({ TeamsMapper.map(it) }, SelectQuery(Teams.name).where(Condition.eq(Teams.ID, id)))

    fun listen(): Observable<List<TeamDb>> =
            dbHelper.query(Function { TeamsMapper.map(it) }, SelectQuery(Teams.name), Teams.name, Drivers.name)


    fun remove(id: Int): Completable = dbHelper.delete(DeleteQuery(Teams.name).where(Condition.eq(Teams.ID, id))).ignoreElement()

    fun removeAll(): Completable = dbHelper.delete(DeleteQuery(Teams.name)).ignoreElement()
}