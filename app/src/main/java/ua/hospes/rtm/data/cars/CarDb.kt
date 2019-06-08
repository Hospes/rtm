package ua.hospes.rtm.data.cars

import android.content.ContentValues
import ua.hospes.dbhelper.IDbModel
import ua.hospes.rtm.core.db.tables.Cars
import ua.hospes.rtm.domain.cars.Car

data class CarDb(
        val id: Int? = null,
        val number: Int,
        val quality: Car.Quality = Car.Quality.NORMAL,
        val broken: Boolean = false
) : IDbModel {
    override fun toContentValues(): ContentValues = ContentValues().apply {
        put(Cars.NUMBER.name(), number)
        put(Cars.QUALITY.name(), quality.toString())
        put(Cars.BROKEN.name(), if (broken) 1 else 0)
    }
}