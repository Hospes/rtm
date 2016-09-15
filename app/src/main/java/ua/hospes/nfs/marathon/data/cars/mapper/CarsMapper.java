package ua.hospes.nfs.marathon.data.cars.mapper;

import android.database.Cursor;

import ua.hospes.nfs.marathon.core.db.tables.Cars;
import ua.hospes.nfs.marathon.data.cars.models.CarDb;
import ua.hospes.nfs.marathon.domain.cars.models.Car;

/**
 * @author Andrew Khloponin
 */
public class CarsMapper {
    public static CarDb map(Cursor cursor) {
        CarDb car = new CarDb(cursor.getInt(cursor.getColumnIndex(Cars._ID)));
        car.setNumber(cursor.getInt(cursor.getColumnIndex(Cars.NUMBER)));
        car.setRating(cursor.getInt(cursor.getColumnIndex(Cars.RATING)));
        return car;
    }

    public static Car map(CarDb carDb) {
        Car car = new Car(carDb.getId());
        car.setNumber(car.getNumber());
        car.setRating(car.getRating());
        return car;
    }

    public static CarDb map(Car car) {
        CarDb carDb = new CarDb(car.getId());
        carDb.setNumber(car.getNumber());
        carDb.setRating(car.getRating());
        return carDb;
    }
}