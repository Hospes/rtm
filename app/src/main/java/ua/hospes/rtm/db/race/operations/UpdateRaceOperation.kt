package ua.hospes.rtm.db.race.operations

import android.content.ContentValues
import android.util.Pair

import com.squareup.sqlbrite2.BriteDatabase

import ua.hospes.dbhelper.Operation
import ua.hospes.dbhelper.builder.UpdateQuery
import ua.hospes.dbhelper.builder.conditions.Condition
import ua.hospes.rtm.db.tables.Race
import ua.hospes.rtm.db.race.RaceItemDb

/**
 * @author Andrew Khloponin
 */
class UpdateRaceOperation : Operation<Boolean> {
    private val query: UpdateQuery
    private val cv: ContentValues

    constructor(item: RaceItemDb) {
        this.cv = item.toContentValues()
        this.query = UpdateQuery(Race.name).where(Condition.eq(Race.ID, item.id))
    }

    constructor(pair: Pair<Int, ContentValues>) {
        this.cv = pair.second
        this.query = UpdateQuery(Race.name).where(Condition.eq(Race.ID, pair.first))
    }

    override fun doOperation(db: BriteDatabase): Boolean? {
        return db.update(query.table, cv, query.whereClause, *query.whereArgs) > 0
    }
}