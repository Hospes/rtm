package ua.hospes.rtm.db.sessions.operations

import android.content.ContentValues

import com.squareup.sqlbrite2.BriteDatabase

import io.reactivex.Observable
import ua.hospes.dbhelper.Operation
import ua.hospes.dbhelper.builder.UpdateQuery
import ua.hospes.dbhelper.builder.conditions.Condition
import ua.hospes.rtm.db.tables.Sessions

/**
 * @author Andrew Khloponin
 */
class CloseSessionOperation private constructor(private val sessionId: Int, private val stopTime: Long) : Operation<Int> {

    override fun doOperation(db: BriteDatabase): Int? {
        val builder = UpdateQuery(Sessions.name).where(Condition.eq(Sessions.ID, sessionId))

        val cv = ContentValues()
        cv.put(Sessions.END_DURATION_TIME.name(), stopTime)

        db.update(builder.table, cv, builder.whereClause, *builder.whereArgs)

        return sessionId
    }

    companion object {


        fun from(stopTime: Long, vararg sessionIds: Int): Observable<CloseSessionOperation> {
            return Observable.create { emitter ->
                for (sessionId in sessionIds)
                    emitter.onNext(CloseSessionOperation(sessionId, stopTime))
                emitter.onComplete()
            }
        }
    }
}