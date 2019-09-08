package ua.hospes.rtm.core.db.race.storage

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import ua.hospes.dbhelper.InsertResult
import ua.hospes.dbhelper.UpdateResult
import ua.hospes.dbhelper.builder.DeleteQuery
import ua.hospes.dbhelper.builder.SelectQuery
import ua.hospes.dbhelper.builder.UpdateQuery
import ua.hospes.dbhelper.builder.conditions.Condition
import ua.hospes.rtm.core.db.DbHelper
import ua.hospes.rtm.core.db.tables.Race
import ua.hospes.rtm.core.db.tables.Sessions
import ua.hospes.rtm.core.db.race.mapper.RaceMapper
import ua.hospes.rtm.core.db.race.models.RaceItemDb
import ua.hospes.rtm.core.db.race.operations.UpdateRaceOperation
import javax.inject.Inject

class RaceDbStorage @Inject
internal constructor(private val dbHelper: DbHelper) {

    fun add(items: List<RaceItemDb>): Observable<InsertResult<RaceItemDb>> = dbHelper.insert(Race.name, SQLiteDatabase.CONFLICT_ABORT, items)

    fun update(item: RaceItemDb): Observable<UpdateResult<RaceItemDb>> =
            dbHelper.update(UpdateQuery(Race.name).where(Condition.eq(Race.ID, item.id)), item)

    fun updateRaces(operations: Iterable<UpdateRaceOperation>): Observable<Boolean> = dbHelper.multiOperationTransaction(operations)


    fun get(): Observable<RaceItemDb> = dbHelper.querySingle({ RaceMapper.map(it) }, SelectQuery(Race.name))

    fun listen(): Observable<List<RaceItemDb>> = dbHelper.query(Function { RaceMapper.map(it) }, SelectQuery(Race.name), Race.name, Sessions.name)

    fun listen(id: Int): Observable<List<RaceItemDb>> =
            dbHelper.query(Function { RaceMapper.map(it) }, SelectQuery(Race.name).where(Condition.eq(Race.ID, id)), Race.name, Sessions.name)

    fun reset(): Observable<Void> {
        val cv = ContentValues()
        cv.put(Race.TEAM_NUMBER.name(), -1)
        cv.put(Race.SESSION_ID.name(), -1)
        return dbHelper.updateCV(UpdateQuery(Race.name), cv).map { null }
    }


    fun remove(session: RaceItemDb): Single<Int> = dbHelper.delete(DeleteQuery(Race.name).where(Condition.eq(Race.ID, session.id)))

    fun removeAll(): Single<Int> = dbHelper.delete(DeleteQuery(Race.name))
}