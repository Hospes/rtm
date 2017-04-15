package ua.hospes.rtm.data.cars.mapper;

import android.database.Cursor;

import ua.hospes.rtm.core.db.tables.Cars;
import ua.hospes.rtm.data.cars.models.CarDb;
import ua.hospes.rtm.domain.cars.models.Car;

/**
 * @author Andrew Khloponin
 */
public class CarsMapper {
    public static CarDb map(Cursor cursor) {
        CarDb db = new CarDb(cursor.getInt(cursor.getColumnIndex(Cars._ID)));
        db.setNumber(cursor.getInt(cursor.getColumnIndex(Cars.NUMBER)));
        db.setRating(cursor.getInt(cursor.getColumnIndex(Cars.RATING)));
        return db;
    }

    public static Car map(CarDb db) {
        Car car = new Car(db.getId());
        car.setNumber(db.getNumber());
        car.setRating(db.getRating());
        return car;
    }

    public static CarDb map(Car car) {
        CarDb db = new CarDb(car.getId());
        db.setNumber(car.getNumber());
        db.setRating(car.getRating());
        return db;
    }
}