package ua.hospes.rtm.domain.cars

import android.os.Parcelable
import androidx.annotation.ColorRes
import kotlinx.parcelize.Parcelize
import ua.hospes.rtm.R
import ua.hospes.rtm.data.model.CarDto

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
            @JvmStatic fun fromString(s: String): Quality = entries.find { it.name.equals(s, ignoreCase = true) } ?: NORMAL
        }
    }
}


private fun Car.Quality.toDto(): CarDto.Quality = when (this) {
    Car.Quality.LOW -> CarDto.Quality.LOW
    Car.Quality.NORMAL -> CarDto.Quality.NORMAL
    Car.Quality.HIGH -> CarDto.Quality.HIGH
}

private fun CarDto.Quality.toDomain(): Car.Quality = when (this) {
    CarDto.Quality.LOW -> Car.Quality.LOW
    CarDto.Quality.NORMAL -> Car.Quality.NORMAL
    CarDto.Quality.HIGH -> Car.Quality.HIGH
}

internal fun CarDto.toDomain(): Car = Car(id, number, quality.toDomain(), broken)
internal fun Car.toDto(): CarDto = CarDto(id, number, quality.toDto(), broken)