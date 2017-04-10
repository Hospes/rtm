package ua.hospes.nfs.marathon.data.race.storage;

import android.content.ContentValues;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.dbhelper.InsertResult;
import ua.hospes.dbhelper.QueryBuilder;
import ua.hospes.dbhelper.UpdateResult;
import ua.hospes.nfs.marathon.core.db.DbHelper;
import ua.hospes.nfs.marathon.core.db.tables.Race;
import ua.hospes.nfs.marathon.core.db.tables.Sessions;
import ua.hospes.nfs.marathon.data.race.mapper.RaceMapper;
import ua.hospes.nfs.marathon.data.race.models.RaceItemDb;
import ua.hospes.nfs.marathon.data.race.operations.UpdateRaceOperation;

/**
 * @author Andrew Khloponin
 */
public class RaceDbStorage {
    private final DbHelper dbHelper;

    @Inject
    public RaceDbStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public Observable<InsertResult<RaceItemDb>> add(List<RaceItemDb> items) {
        return dbHelper.insert(Race.name, items);
    }

    public Observable<UpdateResult<RaceItemDb>> update(RaceItemDb item) {
        return dbHelper.update(new QueryBuilder(Race.name).where(Race._ID + " = ?", String.valueOf(item.getId())), item);
    }

    public Observable<Integer> remove(RaceItemDb session) {
        return dbHelper.delete(new QueryBuilder(Race.name).where(Race._ID + " = ?", String.valueOf(session.getId())));
    }

    public Observable<Boolean> updateRaces(Iterable<UpdateRaceOperation> operations) {
        return dbHelper.multiOperationTransaction(operations);
    }


    public Observable<RaceItemDb> get() {
        return dbHelper.singleQuery(RaceMapper::map, new QueryBuilder(Race.name));
    }

    public Observable<List<RaceItemDb>> listen() {
        return dbHelper.query(RaceMapper::map, new QueryBuilder(Race.name), Race.name, Sessions.name);
    }

    public Observable<Void> reset() {
        ContentValues cv = new ContentValues();
        cv.put(Race.TEAM_NUMBER, -1);
        cv.put(Race.SESSION_ID, -1);
        return dbHelper.update(new QueryBuilder(Race.name), cv).map(result -> null);
    }

    public Observable<Void> clean() {
        return dbHelper.delete(new QueryBuilder(Race.name)).map(integer -> null);
    }
}