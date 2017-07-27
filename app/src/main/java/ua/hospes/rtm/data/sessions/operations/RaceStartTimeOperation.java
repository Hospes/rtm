package ua.hospes.rtm.data.sessions.operations;

import android.content.ContentValues;

import com.squareup.sqlbrite2.BriteDatabase;

import ua.hospes.dbhelper.Operation;
import ua.hospes.dbhelper.builder.UpdateQuery;
import ua.hospes.dbhelper.builder.conditions.Condition;
import ua.hospes.rtm.core.db.tables.Sessions;

/**
 * @author Andrew Khloponin
 */
public class RaceStartTimeOperation implements Operation<Integer> {
    private final int  sessionId;
    private final long startTime;

    public RaceStartTimeOperation(int sessionId, long startTime) {
        this.sessionId = sessionId;
        this.startTime = startTime;
    }

    @Override
    public Integer doOperation(BriteDatabase db) {
        UpdateQuery builder = new UpdateQuery(Sessions.name).where(Condition.eq(Sessions.ID, sessionId));

        ContentValues cv = new ContentValues();
        cv.put(Sessions.RACE_START_TIME.name(), startTime);

        db.update(builder.getTable(), cv, builder.getWhereClause(), builder.getWhereArgs());

        return sessionId;
    }
}