package ua.hospes.rtm.data.race.storage;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ua.hospes.dbhelper.InsertResult;
import ua.hospes.dbhelper.UpdateResult;
import ua.hospes.dbhelper.builder.DeleteQuery;
import ua.hospes.dbhelper.builder.SelectQuery;
import ua.hospes.dbhelper.builder.UpdateQuery;
import ua.hospes.dbhelper.builder.conditions.Condition;
import ua.hospes.rtm.core.db.DbHelper;
import ua.hospes.rtm.core.db.tables.Race;
import ua.hospes.rtm.core.db.tables.Sessions;
import ua.hospes.rtm.data.race.mapper.RaceMapper;
import ua.hospes.rtm.data.race.models.RaceItemDb;
import ua.hospes.rtm.data.race.operations.UpdateRaceOperation;

/**
 * @author Andrew Khloponin
 */
public class RaceDbStorage {
    private final DbHelper dbHelper;

    @Inject
    RaceDbStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public Observable<InsertResult<RaceItemDb>> add(List<RaceItemDb> items) {
        return dbHelper.insert(Race.name, SQLiteDatabase.CONFLICT_ABORT, items);
    }

    public Observable<UpdateResult<RaceItemDb>> update(RaceItemDb item) {
        return dbHelper.update(new UpdateQuery(Race.name).where(Condition.eq(Race.ID, item.getId())), item);
    }

    public Observable<Boolean> updateRaces(Iterable<UpdateRaceOperation> operations) {
        return dbHelper.multiOperationTransaction(operations);
    }


    public Observable<RaceItemDb> get() {
        return dbHelper.querySingle(RaceMapper::map, new SelectQuery(Race.name));
    }

    public Observable<List<RaceItemDb>> listen() {
        return dbHelper.query(RaceMapper::map, new SelectQuery(Race.name), Race.name, Sessions.name);
    }

    public Observable<List<RaceItemDb>> listen(int id) {
        return dbHelper.query(RaceMapper::map, new SelectQuery(Race.name).where(Condition.eq(Race.ID, id)), Race.name, Sessions.name);
    }

    public Observable<Void> reset() {
        ContentValues cv = new ContentValues();
        cv.put(Race.TEAM_NUMBER.name(), -1);
        cv.put(Race.SESSION_ID.name(), -1);
        return dbHelper.updateCV(new UpdateQuery(Race.name), cv).map(result -> null);
    }


    public Single<Integer> remove(RaceItemDb session) {
        return dbHelper.delete(new DeleteQuery(Race.name).where(Condition.eq(Race.ID, session.getId())));
    }

    public Single<Integer> removeAll() {
        return dbHelper.delete(new DeleteQuery(Race.name));
    }
}