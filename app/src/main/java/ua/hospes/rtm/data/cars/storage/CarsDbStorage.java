package ua.hospes.rtm.data.cars.storage;

import android.database.sqlite.SQLiteDatabase;

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
import ua.hospes.rtm.core.db.tables.Cars;
import ua.hospes.rtm.core.db.tables.Sessions;
import ua.hospes.rtm.data.cars.mapper.CarsMapper;
import ua.hospes.rtm.data.cars.models.CarDb;

/**
 * @author Andrew Khloponin
 */
public class CarsDbStorage {
    private final DbHelper dbHelper;

    @Inject
    CarsDbStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public Observable<CarDb> get() {
        return dbHelper.querySingle(CarsMapper::map, new SelectQuery(Cars.name));
    }

    public Observable<CarDb> getByIds(int... ids) {
        return dbHelper.querySingle(CarsMapper::map, new SelectQuery(Cars.name).where(Condition.in(Cars.ID, Ints.asList(ids))));
    }

    public Observable<CarDb> getByNumbers(int... numbers) {
        return dbHelper.querySingle(CarsMapper::map, new SelectQuery(Cars.name).where(Condition.in(Cars.NUMBER, Ints.asList(numbers))));
    }


    public Observable<CarDb> getNotInRace() {
        //SELECT c.* FROM Cars AS c WHERE c._id NOT IN ( SELECT s.car_id FROM Sessions AS s WHERE s.end_time = -1 )
        return dbHelper.querySingle(CarsMapper::map,
                "SELECT c.rowid, c.* FROM " + Cars.name + " c" +
                        " WHERE c." + Cars.ID.name() + " NOT IN " +
                        "(" +
                        " SELECT s." + Sessions.CAR_ID.name() + " FROM " + Sessions.name + " AS s" +
                        " WHERE s." + Sessions.END_DURATION_TIME.name() + " = ?" +
                        " )",
                String.valueOf(-1)
        );
    }


    public Observable<List<CarDb>> listen() {
        return dbHelper.query(CarsMapper::map, new SelectQuery(Cars.name));
    }


    public Observable<InsertResult<CarDb>> add(CarDb car) {
        return dbHelper.insert(Cars.name, SQLiteDatabase.CONFLICT_ABORT, car);
    }

    public Observable<UpdateResult<CarDb>> update(CarDb car) {
        return dbHelper.update(new UpdateQuery(Cars.name).where(Condition.eq(Cars.ID, car.getId())), car);
    }


    public Single<Integer> remove(CarDb carDb) {
        return dbHelper.delete(new DeleteQuery(Cars.name).where(Condition.eq(Cars.ID, carDb.getId())));
    }

    public Single<Void> clear() {
        return dbHelper.delete(new DeleteQuery(Cars.name)).map(integer -> null);
    }
}