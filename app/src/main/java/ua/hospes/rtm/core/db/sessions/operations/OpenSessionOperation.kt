package ua.hospes.rtm.core.db.sessions.operations

import android.content.ContentValues

import com.squareup.sqlbrite2.BriteDatabase

import io.reactivex.Observable
import ua.hospes.dbhelper.Operation
import ua.hospes.dbhelper.builder.UpdateQuery
import ua.hospes.dbhelper.builder.conditions.Condition
import ua.hospes.rtm.core.db.tables.Sessions

/**
 * @author Andrew Khloponin
 */
class OpenSessionOperation(private val sessionId: Int, private val raceStartTime: Long, private val startTime: Long) : Operation<Int> {

    override fun doOperation(db: BriteDatabase): Int? {
        val builder = UpdateQuery(Sessions.name).where(Condition.eq(Sessions.ID, sessionId))

        val cv = ContentValues()
        cv.put(Sessions.RACE_START_TIME.name(), raceStartTime)
        cv.put(Sessions.START_DURATION_TIME.name(), startTime)
        cv.put(Sessions.END_DURATION_TIME.name(), -1)

        db.update(builder.table, cv, builder.whereClause, *builder.whereArgs)

        return sessionId
    }

    companion object {


        fun from(raceStartTime: Long, startTime: Long, vararg sessionIds: Int): Observable<OpenSessionOperation> {
            return Observable.create { emitter ->
                for (sessionId in sessionIds)
                    emitter.onNext(OpenSessionOperation(sessionId, raceStartTime, startTime))
                emitter.onComplete()
            }
        }
    }
}