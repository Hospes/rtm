package ua.hospes.nfs.marathon.data.cars.storage;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.nfs.marathon.core.db.DbHelper;
import ua.hospes.nfs.marathon.core.db.QueryBuilder;
import ua.hospes.nfs.marathon.core.db.models.InsertResult;
import ua.hospes.nfs.marathon.core.db.models.UpdateResult;
import ua.hospes.nfs.marathon.core.db.tables.Cars;
import ua.hospes.nfs.marathon.core.db.tables.Sessions;
import ua.hospes.nfs.marathon.data.cars.mapper.CarsMapper;
import ua.hospes.nfs.marathon.data.cars.models.CarDb;
import ua.hospes.nfs.marathon.utils.ArrayUtils;

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
        return dbHelper.singleQuery(CarsMapper::map, "SELECT t1.* FROM " + Cars.name + " t1 LEFT JOIN " + Sessions.name + " t2 ON t2." + Sessions.CAR_ID + " = t1." + Cars._ID + " WHERE t2." + Sessions.CAR_ID + " IS NULL");
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