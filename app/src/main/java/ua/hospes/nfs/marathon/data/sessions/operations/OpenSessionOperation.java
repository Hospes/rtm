package ua.hospes.nfs.marathon.data.sessions.operations;

import android.content.ContentValues;

import com.squareup.sqlbrite.BriteDatabase;

import ua.hospes.dbhelper.Operation;
import ua.hospes.dbhelper.QueryBuilder;
import ua.hospes.nfs.marathon.core.db.tables.Sessions;

/**
 * @author Andrew Khloponin
 */
public class OpenSessionOperation implements Operation<Integer> {
    private final int sessionId;
    private final long startTime;

    public OpenSessionOperation(int sessionId, long startTime) {
        this.sessionId = sessionId;
        this.startTime = startTime;
    }

    @Override
    public Integer doOperation(BriteDatabase db) {
        QueryBuilder builder = new QueryBuilder(Sessions.name).where(Sessions._ID + " = ?", String.valueOf(sessionId));

        ContentValues cv = new ContentValues();
        cv.put(Sessions.START_DURATION_TIME, startTime);

        db.update(builder.getTable(), cv, builder.getSelection(), builder.getSelectionArgs());

        return sessionId;
    }
}