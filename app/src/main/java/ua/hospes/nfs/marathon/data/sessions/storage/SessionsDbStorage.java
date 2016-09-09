package ua.hospes.nfs.marathon.data.sessions.storage;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.nfs.marathon.core.db.DbHelper;
import ua.hospes.nfs.marathon.core.db.QueryBuilder;
import ua.hospes.nfs.marathon.core.db.models.InsertResult;
import ua.hospes.nfs.marathon.core.db.models.UpdateResult;
import ua.hospes.nfs.marathon.core.db.tables.Sessions;
import ua.hospes.nfs.marathon.data.sessions.mapper.SessionsMapper;
import ua.hospes.nfs.marathon.data.sessions.models.SessionDb;

/**
 * @author Andrew Khloponin
 */
public class SessionsDbStorage {
    private final DbHelper dbHelper;

    @Inject
    public SessionsDbStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public Observable<InsertResult<SessionDb>> add(SessionDb session) {
        return dbHelper.insert(Sessions.name, session);
    }

    public Observable<UpdateResult<SessionDb>> update(SessionDb session) {
        return dbHelper.update(new QueryBuilder(Sessions.name).where(Sessions._ID + " = ?", String.valueOf(session.getId())), session);
    }

    public Observable<Integer> remove(SessionDb session) {
        return dbHelper.delete(new QueryBuilder(Sessions.name).where(Sessions._ID + " = ?", String.valueOf(session.getId())));
    }


    public Observable<SessionDb> get() {
        return dbHelper.singleQuery(SessionsMapper::map, new QueryBuilder(Sessions.name));
    }

    public Observable<List<SessionDb>> listen() {
        return dbHelper.query(SessionsMapper::map, new QueryBuilder(Sessions.name));
    }
}