package ua.hospes.rtm.data.sessions.operations;

import android.content.ContentValues;

import com.squareup.sqlbrite2.BriteDatabase;

import io.reactivex.Observable;
import ua.hospes.dbhelper.Operation;
import ua.hospes.dbhelper.builder.UpdateQuery;
import ua.hospes.dbhelper.builder.conditions.Condition;
import ua.hospes.rtm.core.db.tables.Sessions;

/**
 * @author Andrew Khloponin
 */
public class OpenSessionOperation implements Operation<Integer> {
    private final int sessionId;
    private final long raceStartTime;
    private final long startTime;

    public OpenSessionOperation(int sessionId, long raceStartTime, long startTime) {
        this.sessionId = sessionId;
        this.raceStartTime = raceStartTime;
        this.startTime = startTime;
    }

    @Override
    public Integer doOperation(BriteDatabase db) {
        UpdateQuery builder = new UpdateQuery(Sessions.name).where(Condition.eq(Sessions.ID, sessionId));

        ContentValues cv = new ContentValues();
        cv.put(Sessions.RACE_START_TIME.name(), raceStartTime);
        cv.put(Sessions.START_DURATION_TIME.name(), startTime);
        cv.put(Sessions.END_DURATION_TIME.name(), -1);

        db.update(builder.getTable(), cv, builder.getWhereClause(), builder.getWhereArgs());

        return sessionId;
    }


    public static Observable<OpenSessionOperation> from(long raceStartTime, long startTime, int... sessionIds) {
        return Observable.create(emitter -> {
            for (int sessionId : sessionIds)
                emitter.onNext(new OpenSessionOperation(sessionId, raceStartTime, startTime));
            emitter.onComplete();
        });
    }
}