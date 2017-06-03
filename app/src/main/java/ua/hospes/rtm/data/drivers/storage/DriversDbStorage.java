package ua.hospes.rtm.data.drivers.storage;

import android.content.ContentValues;

import com.google.common.primitives.Ints;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import ua.hospes.dbhelper.InsertResult;
import ua.hospes.dbhelper.UpdateResult;
import ua.hospes.dbhelper.builder.DeleteQuery;
import ua.hospes.dbhelper.builder.SelectQuery;
import ua.hospes.dbhelper.builder.UpdateQuery;
import ua.hospes.dbhelper.builder.conditions.Condition;
import ua.hospes.rtm.core.db.DbHelper;
import ua.hospes.rtm.core.db.tables.Drivers;
import ua.hospes.rtm.data.drivers.mapper.DriversMapper;
import ua.hospes.rtm.data.drivers.models.DriverDb;

/**
 * @author Andrew Khloponin
 */
public class DriversDbStorage {
    private final DbHelper dbHelper;

    @Inject
    DriversDbStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public Observable<InsertResult<DriverDb>> add(DriverDb driver) {
        return dbHelper.insert(Drivers.name, 0, driver);
    }

    public Observable<UpdateResult<DriverDb>> update(DriverDb driver) {
        return dbHelper.update(new UpdateQuery(Drivers.name).where(Condition.eq(Drivers.ID, driver.getId())), driver);
    }


    public Single<Integer> remove(DriverDb driver) {
        return dbHelper.delete(new DeleteQuery(Drivers.name).where(Condition.eq(Drivers.ID, driver.getId())));
    }


    public Observable<DriverDb> get() {
        return dbHelper.querySingle(DriversMapper::map, new SelectQuery(Drivers.name));
    }

    public Observable<DriverDb> get(int... ids) {
        return dbHelper.querySingle(DriversMapper::map, new SelectQuery(Drivers.name).where(Condition.in(Drivers.ID, Ints.asList(ids))));
    }

    public Observable<DriverDb> getTeamById(int teamId) {
        return dbHelper.querySingle(DriversMapper::map, new SelectQuery(Drivers.name).where(Condition.eq(Drivers.TEAM_ID, teamId)));
    }

    public Observable<List<DriverDb>> listen() {
        return dbHelper.query(DriversMapper::map, new SelectQuery(Drivers.name));
    }

    public Observable<Boolean> removeDriversFromTeam(int teamId) {
        ContentValues cv = new ContentValues();
        cv.put(Drivers.TEAM_ID.name(), -1);

        return dbHelper.updateCV(new UpdateQuery(Drivers.name).where(Condition.eq(Drivers.TEAM_ID, teamId)), cv)
                .map(tUpdateResult -> tUpdateResult.getResult() > 0);
    }

    public Observable<Boolean> addDriversToTeam(int teamId, int... driverIds) {
        ContentValues cv = new ContentValues();
        cv.put(Drivers.TEAM_ID.name(), teamId);

        return dbHelper.updateCV(new UpdateQuery(Drivers.name).where(Condition.in(Drivers.ID, Ints.asList(driverIds))), cv)
                .map(tUpdateResult -> tUpdateResult.getResult() > 0);
    }


    public Single<Integer> clear() {
        return dbHelper.delete(new DeleteQuery(Drivers.name));
    }
}