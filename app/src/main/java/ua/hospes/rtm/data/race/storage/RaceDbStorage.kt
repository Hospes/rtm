package ua.hospes.rtm.data.race.storage

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

import javax.inject.Inject

import io.reactivex.Observable
import io.reactivex.Single
import ua.hospes.dbhelper.InsertResult
import ua.hospes.dbhelper.UpdateResult
import ua.hospes.dbhelper.builder.DeleteQuery
import ua.hospes.dbhelper.builder.SelectQuery
import ua.hospes.dbhelper.builder.UpdateQuery
import ua.hospes.dbhelper.builder.conditions.Condition
import ua.hospes.rtm.core.db.DbHelper
import ua.hospes.rtm.core.db.tables.Race
import ua.hospes.rtm.core.db.tables.Sessions
import ua.hospes.rtm.data.race.mapper.RaceMapper
import ua.hospes.rtm.data.race.models.RaceItemDb
import ua.hospes.rtm.data.race.operations.UpdateRaceOperation

/**
 * @author Andrew Khloponin
 */
class RaceDbStorage @Inject
internal constructor(private val dbHelper: DbHelper) {


    fun add(items: List<RaceItemDb>): Observable<InsertResult<RaceItemDb>> {
        return dbHelper.insert(Race.name, SQLiteDatabase.CONFLICT_ABORT, items)
    }

    fun update(item: RaceItemDb): Observable<UpdateResult<RaceItemDb>> {
        return dbHelper.update(UpdateQuery(Race.name).where(Condition.eq(Race.ID, item.id)), item)
    }

    fun updateRaces(operations: Iterable<UpdateRaceOperation>): Observable<Boolean> {
        return dbHelper.multiOperationTransaction(operations)
    }


    fun get(): Observable<RaceItemDb> {
        return dbHelper.querySingle(Function<Cursor, RaceItemDb> { RaceMapper.map(it) }, SelectQuery(Race.name))
    }

    fun listen(): Observable<List<RaceItemDb>> {
        return dbHelper.query(Function<Cursor, RaceItemDb> { RaceMapper.map(it) }, SelectQuery(Race.name), Race.name, Sessions.name)
    }

    fun listen(id: Int): Observable<List<RaceItemDb>> {
        return dbHelper.query(Function<Cursor, RaceItemDb> { RaceMapper.map(it) }, SelectQuery(Race.name).where(Condition.eq(Race.ID, id)), Race.name, Sessions.name)
    }

    fun reset(): Observable<Void> {
        val cv = ContentValues()
        cv.put(Race.TEAM_NUMBER.name(), -1)
        cv.put(Race.SESSION_ID.name(), -1)
        return dbHelper.updateCV(UpdateQuery(Race.name), cv).map { result -> null }
    }


    fun remove(session: RaceItemDb): Single<Int> {
        return dbHelper.delete(DeleteQuery(Race.name).where(Condition.eq(Race.ID, session.id)))
    }

    fun removeAll(): Single<Int> {
        return dbHelper.delete(DeleteQuery(Race.name))
    }
}