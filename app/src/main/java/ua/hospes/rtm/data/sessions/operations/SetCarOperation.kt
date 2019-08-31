package ua.hospes.rtm.data.sessions.operations

import android.content.ContentValues

import com.squareup.sqlbrite2.BriteDatabase

import ua.hospes.dbhelper.Operation
import ua.hospes.dbhelper.builder.UpdateQuery
import ua.hospes.dbhelper.builder.conditions.Condition
import ua.hospes.rtm.core.db.tables.Sessions

/**
 * @author Andrew Khloponin
 */
class SetCarOperation(private val sessionId: Int, carId: Int) : Operation<Int> {
    private val carId: Long

    init {
        this.carId = carId.toLong()
    }

    override fun doOperation(db: BriteDatabase): Int? {
        val builder = UpdateQuery(Sessions.name).where(Condition.eq(Sessions.ID, sessionId))

        val cv = ContentValues()
        cv.put(Sessions.CAR_ID.name(), carId)

        db.update(builder.table, cv, builder.whereClause, *builder.whereArgs)

        return sessionId
    }
}