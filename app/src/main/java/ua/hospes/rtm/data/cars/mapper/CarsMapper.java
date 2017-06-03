package ua.hospes.rtm.data.cars.mapper;

import android.database.Cursor;

import ua.hospes.rtm.core.db.tables.Cars;
import ua.hospes.rtm.data.cars.models.CarDb;
import ua.hospes.rtm.domain.cars.models.Car;
import ua.hospes.rtm.domain.cars.models.CarQuality;

/**
 * @author Andrew Khloponin
 */
public class CarsMapper {
    public static CarDb map(Cursor cursor) {
        CarDb db = new CarDb(cursor.getInt(cursor.getColumnIndex(Cars.ID.name())));
        db.setNumber(cursor.getInt(cursor.getColumnIndex(Cars.NUMBER.name())));
        db.setQuality(CarQuality.fromString(cursor.getString(cursor.getColumnIndex(Cars.QUALITY.name()))));
        db.setBroken(cursor.getInt(cursor.getColumnIndex(Cars.BROKEN.name())) == 1);
        return db;
    }

    public static Car map(CarDb db) {
        Car car = new Car(db.getId());
        car.setNumber(db.getNumber());
        car.setQuality(db.getQuality());
        car.setBroken(db.isBroken());
        return car;
    }

    public static CarDb map(Car car) {
        CarDb db = new CarDb(car.getId());
        db.setNumber(car.getNumber());
        db.setQuality(car.getQuality());
        db.setBroken(car.isBroken());
        return db;
    }
}