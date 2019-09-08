package ua.hospes.rtm.core.db.sessions.operations

import android.content.ContentValues

import com.squareup.sqlbrite2.BriteDatabase

import ua.hospes.dbhelper.Operation
import ua.hospes.dbhelper.builder.UpdateQuery
import ua.hospes.dbhelper.builder.conditions.Condition
import ua.hospes.rtm.core.db.tables.Sessions

/**
 * @author Andrew Khloponin
 */
class SetDriverOperation(private val sessionId: Int, driverId: Int) : Operation<Int> {
    private val driverId: Long

    init {
        this.driverId = driverId.toLong()
    }

    override fun doOperation(db: BriteDatabase): Int? {
        val builder = UpdateQuery(Sessions.name).where(Condition.eq(Sessions.ID, sessionId))

        val cv = ContentValues()
        cv.put(Sessions.DRIVER_ID.name(), driverId)

        db.update(builder.table, cv, builder.whereClause, *builder.whereArgs)

        return sessionId
    }
}