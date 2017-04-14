package ua.hospes.nfs.marathon.data.sessions.storage;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.dbhelper.InsertResult;
import ua.hospes.dbhelper.QueryBuilder;
import ua.hospes.dbhelper.UpdateResult;
import ua.hospes.nfs.marathon.core.db.DbHelper;
import ua.hospes.nfs.marathon.core.db.tables.Sessions;
import ua.hospes.nfs.marathon.data.sessions.mapper.SessionsMapper;
import ua.hospes.nfs.marathon.data.sessions.models.SessionDb;
import ua.hospes.nfs.marathon.data.sessions.operations.CloseSessionOperation;
import ua.hospes.nfs.marathon.data.sessions.operations.OpenSessionOperation;
import ua.hospes.nfs.marathon.data.sessions.operations.RaceStartTimeOperation;
import ua.hospes.nfs.marathon.data.sessions.operations.SetCarOperation;
import ua.hospes.nfs.marathon.data.sessions.operations.SetDriverOperation;
import ua.hospes.nfs.marathon.utils.ArrayUtils;

/**
 * @author Andrew Khloponin
 */
public class SessionsDbStorage {
    private final DbHelper dbHelper;

    @Inject
    public SessionsDbStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public Observable<InsertResult<SessionDb>> add(List<SessionDb> sessions) {
        return dbHelper.insert(Sessions.name, sessions)
                .map(result -> {
                    result.getData().setId((int) result.getResult());
                    return result;
                });
    }

    public Observable<UpdateResult<SessionDb>> update(SessionDb session) {
        return dbHelper.update(new QueryBuilder(Sessions.name).where(Sessions._ID + " = ?", String.valueOf(session.getId())), session);
    }

    public Observable<Integer> remove(SessionDb session) {
        return dbHelper.delete(new QueryBuilder(Sessions.name).where(Sessions._ID + " = ?", String.valueOf(session.getId())));
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
        return dbHelper.singleQuery(SessionsMapper::map, new QueryBuilder(Sessions.name));
    }

    public Observable<SessionDb> get(int... ids) {
        return dbHelper.singleQuery(SessionsMapper::map, new QueryBuilder(Sessions.name).whereIn(Sessions._ID, ArrayUtils.convert(ids)));
    }

    public Observable<SessionDb> getByTeamId(int teamId) {
        return dbHelper.singleQuery(SessionsMapper::map, new QueryBuilder(Sessions.name).where(Sessions.TEAM_ID + " = ?", String.valueOf(teamId)));
    }

    public Observable<SessionDb> getByTeamIdAndDriverId(int teamId, int driverId) {
        return dbHelper.singleQuery(SessionsMapper::map, new QueryBuilder(Sessions.name)
                .where(Sessions.TEAM_ID + " = ?", String.valueOf(teamId))
                .and()
                .where(Sessions.DRIVER_ID + " = ?", String.valueOf(driverId))
        );
    }

    public Observable<List<SessionDb>> listen() {
        return dbHelper.query(SessionsMapper::map, new QueryBuilder(Sessions.name));
    }

    public Observable<List<SessionDb>> listenByTeamId(int teamId) {
        return dbHelper.query(SessionsMapper::map, new QueryBuilder(Sessions.name).where(Sessions.TEAM_ID + " = ?", String.valueOf(teamId)));
    }

    public Observable<Void> clean() {
        return dbHelper.delete(new QueryBuilder(Sessions.name)).map(integer -> null);
    }
}