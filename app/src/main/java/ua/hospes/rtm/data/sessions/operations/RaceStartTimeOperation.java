package ua.hospes.rtm.data.sessions.operations;

import android.content.ContentValues;

import com.squareup.sqlbrite.BriteDatabase;

import ua.hospes.dbhelper.Operation;
import ua.hospes.dbhelper.QueryBuilder;
import ua.hospes.rtm.core.db.tables.Sessions;

/**
 * @author Andrew Khloponin
 */
public class RaceStartTimeOperation implements Operation<Integer> {
    private final int sessionId;
    private final long startTime;

    public RaceStartTimeOperation(int sessionId, long startTime) {
        this.sessionId = sessionId;
        this.startTime = startTime;
    }

    @Override
    public Integer doOperation(BriteDatabase db) {
        QueryBuilder builder = new QueryBuilder(Sessions.name).where(Sessions._ID + " = ?", String.valueOf(sessionId));

        ContentValues cv = new ContentValues();
        cv.put(Sessions.RACE_START_TIME, startTime);

        db.update(builder.getTable(), cv, builder.getSelection(), builder.getSelectionArgs());

        return sessionId;
    }
}