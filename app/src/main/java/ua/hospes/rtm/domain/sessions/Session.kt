package ua.hospes.rtm.domain.sessions

import ua.hospes.rtm.data.model.SessionDto
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.cars.toDomain
import ua.hospes.rtm.domain.cars.toDto
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.drivers.toDomain
import ua.hospes.rtm.domain.drivers.toDto

data class Session(
    val id: Long = 0,
    val teamId: Long,
    val driver: Driver? = null,
    val car: Car? = null,
    val raceStartTime: Long? = null,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val type: Type = Type.TRACK
) {
    enum class Type {
        TRACK,
        PIT
    }
}


private fun Session.Type.toDto(): SessionDto.Type = when (this) {
    Session.Type.TRACK -> SessionDto.Type.TRACK
    Session.Type.PIT -> SessionDto.Type.PIT
}

internal fun SessionDto.Type.toDomain(): Session.Type = when (this) {
    SessionDto.Type.TRACK -> Session.Type.TRACK
    SessionDto.Type.PIT -> Session.Type.PIT
}

internal fun Session.toDto(): SessionDto = SessionDto(
    id = id,
    teamId = teamId,
    driver = driver?.toDto(),
    car = car?.toDto(),
    raceStartTime = raceStartTime,
    startTime = startTime,
    endTime = endTime,
    type = type.toDto(),
)

internal fun SessionDto.toDomain(): Session = Session(
    id = id,
    teamId = teamId,
    driver = driver?.toDomain(),
    car = car?.toDomain(),
    raceStartTime = raceStartTime,
    startTime = startTime,
    endTime = endTime,
    type = type.toDomain(),
)