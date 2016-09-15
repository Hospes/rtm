package ua.hospes.nfs.marathon.data.sessions.operations;

import android.content.ContentValues;

import com.squareup.sqlbrite.BriteDatabase;

import ua.hospes.nfs.marathon.core.db.Operation;
import ua.hospes.nfs.marathon.core.db.QueryBuilder;
import ua.hospes.nfs.marathon.core.db.tables.Sessions;

/**
 * @author Andrew Khloponin
 */
public class SetDriverOperation implements Operation<Integer> {
    private final int sessionId;
    private final long driverId;

    public SetDriverOperation(int sessionId, int driverId) {
        this.sessionId = sessionId;
        this.driverId = driverId;
    }

    @Override
    public Integer doOperation(BriteDatabase db) {
        QueryBuilder builder = new QueryBuilder(Sessions.name).where(Sessions._ID + " = ?", String.valueOf(sessionId));

        ContentValues cv = new ContentValues();
        cv.put(Sessions.DRIVER_ID, driverId);

        db.update(builder.getTable(), cv, builder.getSelection(), builder.getSelectionArgs());

        return sessionId;
    }
}