package ua.hospes.nfs.marathon.data.drivers.storage;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.nfs.marathon.core.db.DbHelper;
import ua.hospes.nfs.marathon.core.db.QueryBuilder;
import ua.hospes.nfs.marathon.core.db.models.InsertResult;
import ua.hospes.nfs.marathon.core.db.models.UpdateResult;
import ua.hospes.nfs.marathon.core.db.tables.Drivers;
import ua.hospes.nfs.marathon.data.drivers.mapper.DriversMapper;
import ua.hospes.nfs.marathon.data.drivers.models.DriverDb;
import ua.hospes.nfs.marathon.utils.ArrayUtils;

/**
 * @author Andrew Khloponin
 */
public class DriversDbStorage {
    private final DbHelper dbHelper;

    @Inject
    public DriversDbStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public Observable<InsertResult<DriverDb>> add(DriverDb driver) {
        return dbHelper.insert(Drivers.name, driver);
    }

    public Observable<UpdateResult<DriverDb>> update(DriverDb driver) {
        return dbHelper.update(new QueryBuilder(Drivers.name).where(Drivers._ID + " = ?", String.valueOf(driver.getId())), driver);
    }


    public Observable<Integer> remove(DriverDb driver) {
        return dbHelper.delete(new QueryBuilder(Drivers.name).where(Drivers._ID + " = ?", String.valueOf(driver.getId())));
    }


    public Observable<DriverDb> get() {
        return dbHelper.singleQuery(DriversMapper::map, new QueryBuilder(Drivers.name));
    }

    public Observable<DriverDb> get(int... ids) {
        return dbHelper.singleQuery(DriversMapper::map, new QueryBuilder(Drivers.name).whereIn(Drivers._ID, ArrayUtils.convert(ids)));
    }

    public Observable<DriverDb> getTeamById(int teamId) {
        return dbHelper.singleQuery(DriversMapper::map, new QueryBuilder(Drivers.name).where(Drivers.TEAM_ID + " = ?", String.valueOf(teamId)));
    }

    public Observable<List<DriverDb>> listen() {
        return dbHelper.query(DriversMapper::map, new QueryBuilder(Drivers.name));
    }


    public Observable<Void> clear() {
        return dbHelper.delete(new QueryBuilder(Drivers.name)).map(integer -> null);
    }
}