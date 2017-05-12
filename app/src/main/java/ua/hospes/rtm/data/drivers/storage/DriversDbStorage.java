package ua.hospes.rtm.data.drivers.storage;

import android.content.ContentValues;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.dbhelper.InsertResult;
import ua.hospes.dbhelper.QueryBuilder;
import ua.hospes.dbhelper.UpdateResult;
import ua.hospes.rtm.core.db.DbHelper;
import ua.hospes.rtm.core.db.tables.Drivers;
import ua.hospes.rtm.data.drivers.mapper.DriversMapper;
import ua.hospes.rtm.data.drivers.models.DriverDb;
import ua.hospes.rtm.utils.ArrayUtils;

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

    public Observable<Boolean> removeDriversFromTeam(int teamId) {
        ContentValues cv = new ContentValues();
        cv.put(Drivers.TEAM_ID, -1);

        return dbHelper.update(new QueryBuilder(Drivers.name).where(Drivers.TEAM_ID + " = ?", String.valueOf(teamId)), cv)
                .map(tUpdateResult -> tUpdateResult.getResult() > 0);
    }

    public Observable<Boolean> addDriversToTeam(int teamId, String... driverIds) {
        ContentValues cv = new ContentValues();
        cv.put(Drivers.TEAM_ID, teamId);

        return dbHelper.update(new QueryBuilder(Drivers.name).whereIn(Drivers._ID, driverIds), cv)
                .map(tUpdateResult -> tUpdateResult.getResult() > 0);
    }


    public Observable<Void> clear() {
        return dbHelper.delete(new QueryBuilder(Drivers.name)).map(integer -> null);
    }
}