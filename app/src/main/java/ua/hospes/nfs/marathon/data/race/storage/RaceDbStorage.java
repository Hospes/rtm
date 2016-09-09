package ua.hospes.nfs.marathon.data.race.storage;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.nfs.marathon.core.db.DbHelper;
import ua.hospes.nfs.marathon.core.db.QueryBuilder;
import ua.hospes.nfs.marathon.core.db.models.InsertResult;
import ua.hospes.nfs.marathon.core.db.models.UpdateResult;
import ua.hospes.nfs.marathon.core.db.tables.Race;
import ua.hospes.nfs.marathon.data.race.mapper.RaceMapper;
import ua.hospes.nfs.marathon.data.race.models.RaceItemDb;

/**
 * @author Andrew Khloponin
 */
public class RaceDbStorage {
    private final DbHelper dbHelper;

    @Inject
    public RaceDbStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public Observable<InsertResult<RaceItemDb>> add(RaceItemDb item) {
        return dbHelper.insert(Race.name, item);
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


    public Observable<RaceItemDb> get() {
        return dbHelper.singleQuery(RaceMapper::map, new QueryBuilder(Race.name));
    }

    public Observable<List<RaceItemDb>> listen() {
        return dbHelper.query(RaceMapper::map, new QueryBuilder(Race.name));
    }
}