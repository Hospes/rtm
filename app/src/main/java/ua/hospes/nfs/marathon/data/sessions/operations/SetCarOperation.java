package ua.hospes.nfs.marathon.data.sessions.operations;

import android.content.ContentValues;

import com.squareup.sqlbrite.BriteDatabase;

import ua.hospes.nfs.marathon.core.db.Operation;
import ua.hospes.nfs.marathon.core.db.QueryBuilder;
import ua.hospes.nfs.marathon.core.db.tables.Sessions;

/**
 * @author Andrew Khloponin
 */
public class SetCarOperation implements Operation<Integer> {
    private final int sessionId;
    private final long carId;

    public SetCarOperation(int sessionId, int carId) {
        this.sessionId = sessionId;
        this.carId = carId;
    }

    @Override
    public Integer doOperation(BriteDatabase db) {
        QueryBuilder builder = new QueryBuilder(Sessions.name).where(Sessions._ID + " = ?", String.valueOf(sessionId));

        ContentValues cv = new ContentValues();
        cv.put(Sessions.CAR_ID, carId);

        db.update(builder.getTable(), cv, builder.getSelection(), builder.getSelectionArgs());

        return sessionId;
    }
}