package ua.hospes.rtm.data.sessions.operations;

import android.content.ContentValues;

import com.squareup.sqlbrite.BriteDatabase;

import ua.hospes.dbhelper.Operation;
import ua.hospes.dbhelper.builder.UpdateQuery;
import ua.hospes.dbhelper.builder.conditions.Condition;
import ua.hospes.rtm.core.db.tables.Sessions;

/**
 * @author Andrew Khloponin
 */
public class CloseSessionOperation implements Operation<Integer> {
    private final int sessionId;
    private final long stopTime;

    public CloseSessionOperation(int sessionId, long stopTime) {
        this.sessionId = sessionId;
        this.stopTime = stopTime;
    }

    @Override
    public Integer doOperation(BriteDatabase db) {
        UpdateQuery builder = new UpdateQuery(Sessions.name).where(Condition.eq(Sessions.ID, sessionId));

        ContentValues cv = new ContentValues();
        cv.put(Sessions.END_DURATION_TIME.name(), stopTime);

        db.update(builder.getTable(), cv, builder.getWhereClause(), builder.getWhereArgs());

        return sessionId;
    }
}