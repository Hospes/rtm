package ua.hospes.rtm.data.sessions.storage;

import android.database.sqlite.SQLiteDatabase;

import com.google.common.primitives.Ints;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.dbhelper.InsertResult;
import ua.hospes.dbhelper.UpdateResult;
import ua.hospes.dbhelper.builder.DeleteQuery;
import ua.hospes.dbhelper.builder.SelectQuery;
import ua.hospes.dbhelper.builder.UpdateQuery;
import ua.hospes.dbhelper.builder.conditions.Condition;
import ua.hospes.rtm.core.db.DbHelper;
import ua.hospes.rtm.core.db.tables.Sessions;
import ua.hospes.rtm.data.sessions.mapper.SessionsMapper;
import ua.hospes.rtm.data.sessions.models.SessionDb;
import ua.hospes.rtm.data.sessions.operations.CloseSessionOperation;
import ua.hospes.rtm.data.sessions.operations.OpenSessionOperation;
import ua.hospes.rtm.data.sessions.operations.RaceStartTimeOperation;
import ua.hospes.rtm.data.sessions.operations.SetCarOperation;
import ua.hospes.rtm.data.sessions.operations.SetDriverOperation;

/**
 * @author Andrew Khloponin
 */
public class SessionsDbStorage {
    private final DbHelper dbHelper;

    @Inject
    SessionsDbStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public Observable<InsertResult<SessionDb>> add(List<SessionDb> sessions) {
        return dbHelper.insert(Sessions.name, SQLiteDatabase.CONFLICT_ABORT, sessions).map(result -> {
            result.getData().setId((int) result.getResult());
            return result;
        });
    }

    public Observable<UpdateResult<SessionDb>> update(SessionDb session) {
        return dbHelper.update(new UpdateQuery(Sessions.name).where(Condition.eq(Sessions.ID, session.getId())), session);
    }

    public Observable<Integer> remove(SessionDb session) {
        return dbHelper.delete(new DeleteQuery(Sessions.name).where(Condition.eq(Sessions.ID, session.getId()))).toObservable();
    }


    public Observable<Integer> applySetDriverOperations(Iterable<SetDriverOperation> operations) {
        return dbHelper.multiOperationTransaction(operations);
    }

    public Observable<Integer> applySetCarOperations(Iterable<SetCarOperation> operations) {
        return dbHelper.multiOperationTransaction(operations);
    }

    public Observable<Integer> applyOpenOperations(Iterable<OpenSessionOperation> operations) {
        return dbHelper.multiOperationTransaction(operations);
    }

    public Observable<Integer> applyCloseOperations(Iterable<CloseSessionOperation> operations) {
        return dbHelper.multiOperationTransaction(operations);
    }

    public Observable<Integer> applyRaceStartOperations(Iterable<RaceStartTimeOperation> operations) {
        return dbHelper.multiOperationTransaction(operations);
    }


    public Observable<SessionDb> get() {
        return dbHelper.querySingle(SessionsMapper::map, new SelectQuery(Sessions.name));
    }

    public Observable<SessionDb> get(int... ids) {
        return dbHelper.querySingle(SessionsMapper::map, new SelectQuery(Sessions.name).where(Condition.in(Sessions.ID, Ints.asList(ids))));
    }

    public Observable<SessionDb> getByTeamId(int teamId) {
        return dbHelper.querySingle(SessionsMapper::map, new SelectQuery(Sessions.name).where(Condition.eq(Sessions.TEAM_ID, teamId)));
    }

    public Observable<SessionDb> getByTeamIdAndDriverId(int teamId, int driverId) {
        return dbHelper.querySingle(SessionsMapper::map, new SelectQuery(Sessions.name)
                .where(Condition.eq(Sessions.TEAM_ID, teamId))
                .where(Condition.eq(Sessions.DRIVER_ID, driverId))
        );
    }

    public Observable<List<SessionDb>> listen() {
        return dbHelper.query(SessionsMapper::map, new SelectQuery(Sessions.name));
    }

    public Observable<List<SessionDb>> listenByTeamId(int teamId) {
        return dbHelper.query(SessionsMapper::map, new SelectQuery(Sessions.name).where(Condition.eq(Sessions.TEAM_ID, teamId)));
    }

    public Observable<Void> clean() {
        return dbHelper.delete(new DeleteQuery(Sessions.name)).toObservable().map(integer -> null);
    }
}