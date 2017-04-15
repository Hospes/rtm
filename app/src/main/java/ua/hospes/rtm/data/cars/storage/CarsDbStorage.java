package ua.hospes.rtm.data.cars.storage;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.dbhelper.InsertResult;
import ua.hospes.dbhelper.QueryBuilder;
import ua.hospes.dbhelper.UpdateResult;
import ua.hospes.rtm.core.db.DbHelper;
import ua.hospes.rtm.core.db.tables.Cars;
import ua.hospes.rtm.core.db.tables.Sessions;
import ua.hospes.rtm.data.cars.mapper.CarsMapper;
import ua.hospes.rtm.data.cars.models.CarDb;
import ua.hospes.rtm.utils.ArrayUtils;

/**
 * @author Andrew Khloponin
 */
public class CarsDbStorage {
    private final DbHelper dbHelper;

    @Inject
    public CarsDbStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public Observable<CarDb> get() {
        return dbHelper.singleQuery(CarsMapper::map, new QueryBuilder(Cars.name));
    }

    public Observable<CarDb> getByIds(int... ids) {
        return dbHelper.singleQuery(CarsMapper::map, new QueryBuilder(Cars.name).whereIn(Cars._ID, ArrayUtils.convert(ids)));
    }

    public Observable<CarDb> getByNumbers(int... numbers) {
        return dbHelper.singleQuery(CarsMapper::map, new QueryBuilder(Cars.name).whereIn(Cars.NUMBER, ArrayUtils.convert(numbers)));
    }


    public Observable<CarDb> getNotInRace() {
        //SELECT c.* FROM Cars AS c WHERE c._id NOT IN ( SELECT s.car_id FROM Sessions AS s WHERE s.end_time = -1 )
        return dbHelper.singleQuery(CarsMapper::map,
                "SELECT c.rowid, c.* FROM " + Cars.name + " c" +
                        " WHERE c." + Cars._ID + " NOT IN " +
                        "(" +
                        " SELECT s." + Sessions.CAR_ID + " FROM " + Sessions.name + " AS s" +
                        " WHERE s." + Sessions.END_DURATION_TIME + " = ?" +
                        " )",
                String.valueOf(-1)
        );
    }


    public Observable<List<CarDb>> listen() {
        return dbHelper.query(CarsMapper::map, new QueryBuilder(Cars.name));
    }


    public Observable<InsertResult<CarDb>> add(CarDb car) {
        return dbHelper.insert(Cars.name, car);
    }

    public Observable<UpdateResult<CarDb>> update(CarDb car) {
        return dbHelper.update(new QueryBuilder(Cars.name).where(Cars._ID + " = ?", String.valueOf(car.getId())), car);
    }


    public Observable<Integer> remove(CarDb carDb) {
        return dbHelper.delete(new QueryBuilder(Cars.name).where(Cars._ID + " = ?", String.valueOf(carDb.getId())));
    }

    public Observable<Void> clear() {
        return dbHelper.delete(new QueryBuilder(Cars.name)).map(integer -> null);
    }
}