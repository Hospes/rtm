package ua.hospes.rtm.domain.cars

import android.os.Parcelable
import androidx.annotation.ColorRes
import kotlinx.parcelize.Parcelize
import ua.hospes.rtm.R
import ua.hospes.rtm.data.db.cars.CarEntity

@Parcelize
data class Car(
    val id: Long = 0,
    val number: Int,
    val quality: Quality = Quality.NORMAL,
    val broken: Boolean = false
) : Parcelable {

    enum class Quality(@ColorRes val color: Int) {
        LOW(R.color.car_quality_low),
        NORMAL(R.color.car_quality_normal),
        HIGH(R.color.car_quality_high);

        companion object {
            @JvmStatic fun fromString(s: String): Quality = values().find { it.name.equals(s, ignoreCase = true) } ?: NORMAL
        }
    }
}

fun CarEntity.toDomain(): Car = Car(id, number, quality, broken)

fun Car.toDbEntity(): CarEntity = CarEntity(id, number, quality, broken)