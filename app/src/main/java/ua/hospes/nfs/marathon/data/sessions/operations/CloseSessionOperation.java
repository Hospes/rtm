package ua.hospes.nfs.marathon.data.sessions.operations;

import android.content.ContentValues;

import com.squareup.sqlbrite.BriteDatabase;

import ua.hospes.nfs.marathon.core.db.Operation;
import ua.hospes.nfs.marathon.core.db.QueryBuilder;
import ua.hospes.nfs.marathon.core.db.tables.Sessions;

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
        QueryBuilder builder = new QueryBuilder(Sessions.name).where(Sessions._ID + " = ?", String.valueOf(sessionId));

        ContentValues cv = new ContentValues();
        cv.put(Sessions.END_DURATION_TIME, stopTime);

        db.update(builder.getTable(), cv, builder.getSelection(), builder.getSelectionArgs());

        return sessionId;
    }
}