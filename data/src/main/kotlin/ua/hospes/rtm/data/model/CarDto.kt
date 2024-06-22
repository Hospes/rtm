package ua.hospes.rtm.data.model

import ua.hospes.rtm.data.db.cars.CarEntity

data class CarDto(
    val id: Long = 0,
    val number: Int,
    val quality: Quality = Quality.NORMAL,
    val broken: Boolean = false
) {
    enum class Quality { LOW, NORMAL, HIGH }
}

private fun CarEntity.Quality.toDto(): CarDto.Quality = when (this) {
    CarEntity.Quality.LOW -> CarDto.Quality.LOW
    CarEntity.Quality.NORMAL -> CarDto.Quality.NORMAL
    CarEntity.Quality.HIGH -> CarDto.Quality.HIGH
}

private fun CarDto.Quality.toEntity(): CarEntity.Quality = when (this) {
    CarDto.Quality.LOW -> CarEntity.Quality.LOW
    CarDto.Quality.NORMAL -> CarEntity.Quality.NORMAL
    CarDto.Quality.HIGH -> CarEntity.Quality.HIGH
}

internal fun CarEntity.toDto(): CarDto = CarDto(id, number, quality.toDto(), broken)
internal fun CarDto.toDbEntity(): CarEntity = CarEntity(id, number, quality.toEntity(), broken)