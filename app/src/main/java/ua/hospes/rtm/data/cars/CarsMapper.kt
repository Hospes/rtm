package ua.hospes.rtm.data.cars

import android.database.Cursor
import ua.hospes.rtm.core.db.tables.Cars
import ua.hospes.rtm.domain.cars.Car

object CarsMapper {
    @JvmStatic fun map(cursor: Cursor): CarDb = CarDb(
            cursor.getInt(cursor.getColumnIndex(Cars.ID.name())),
            cursor.getInt(cursor.getColumnIndex(Cars.NUMBER.name())),
            Car.Quality.fromString(cursor.getString(cursor.getColumnIndex(Cars.QUALITY.name()))),
            cursor.getInt(cursor.getColumnIndex(Cars.BROKEN.name())) == 1
    )

    @JvmStatic fun map(db: CarDb): Car = Car(db.id, db.number, db.quality, db.broken)

    @JvmStatic fun map(car: Car): CarDb = CarDb(car.id, car.number, car.quality, car.broken)
}