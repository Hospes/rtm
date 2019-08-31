package ua.hospes.rtm.data.sessions.storage

import android.database.sqlite.SQLiteDatabase
import io.reactivex.Observable
import io.reactivex.Single
import ua.hospes.dbhelper.InsertResult
import ua.hospes.dbhelper.UpdateResult
import ua.hospes.dbhelper.builder.DeleteQuery
import ua.hospes.dbhelper.builder.SelectQuery
import ua.hospes.dbhelper.builder.UpdateQuery
import ua.hospes.dbhelper.builder.conditions.Condition
import ua.hospes.rtm.core.db.DbHelper
import ua.hospes.rtm.core.db.tables.Sessions
import ua.hospes.rtm.data.sessions.mapper.SessionsMapper
import ua.hospes.rtm.data.sessions.models.SessionDb
import ua.hospes.rtm.data.sessions.operations.*
import javax.inject.Inject

class SessionsDbStorage @Inject constructor(private val dbHelper: DbHelper) {

    fun add(sessions: List<SessionDb>): Observable<InsertResult<SessionDb>> {
        return dbHelper.insert(Sessions.name, SQLiteDatabase.CONFLICT_ABORT, sessions).map { result ->
            result.data.id = result.result.toInt()
            result
        }
    }

    fun update(session: SessionDb): Observable<UpdateResult<SessionDb>> =
            dbHelper.update(UpdateQuery(Sessions.name).where(Condition.eq(Sessions.ID, session.id)), session)


    fun applySetDriverOperations(operations: Iterable<SetDriverOperation>): Observable<Int> =
            dbHelper.multiOperationTransaction(operations)

    fun applySetCarOperations(operations: Iterable<SetCarOperation>): Observable<Int> =
            dbHelper.multiOperationTransaction(operations)

    fun applyOpenOperations(operations: Iterable<OpenSessionOperation>): Observable<Int> =
            dbHelper.multiOperationTransaction(operations)

    fun applyCloseOperations(operations: Iterable<CloseSessionOperation>): Observable<Int> =
            dbHelper.multiOperationTransaction(operations)

    fun applyRaceStartOperations(operations: Iterable<RaceStartTimeOperation>): Observable<Int> =
            dbHelper.multiOperationTransaction(operations)


    fun get(): Observable<SessionDb> = dbHelper.querySingle({ SessionsMapper.map(it) }, SelectQuery(Sessions.name))

    operator fun get(vararg ids: Int): Observable<SessionDb> =
            dbHelper.querySingle({ SessionsMapper.map(it) }, SelectQuery(Sessions.name).where(Condition.`in`(Sessions.ID, ids.asList())))

    fun getByTeamId(teamId: Int): Observable<SessionDb> =
            dbHelper.querySingle({ SessionsMapper.map(it) }, SelectQuery(Sessions.name).where(Condition.eq(Sessions.TEAM_ID, teamId)))

    fun getByTeamIdAndDriverId(teamId: Int, driverId: Int): Observable<SessionDb> =
            dbHelper.querySingle({ SessionsMapper.map(it) }, SelectQuery(Sessions.name)
                    .where(Condition.eq(Sessions.TEAM_ID, teamId))
                    .where(Condition.eq(Sessions.DRIVER_ID, driverId)))

    fun listen(): Observable<List<SessionDb>> = dbHelper.query({ SessionsMapper.map(it) }, SelectQuery(Sessions.name))

    fun listenByTeamId(teamId: Int): Observable<List<SessionDb>> =
            dbHelper.query({ SessionsMapper.map(it) }, SelectQuery(Sessions.name).where(Condition.eq(Sessions.TEAM_ID, teamId)))


    fun remove(session: SessionDb): Single<Int> = dbHelper.delete(DeleteQuery(Sessions.name).where(Condition.eq(Sessions.ID, session.id)))

    fun removeAll(): Single<Int> = dbHelper.delete(DeleteQuery(Sessions.name))
}