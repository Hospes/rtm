package ua.hospes.rtm.data.model

import ua.hospes.rtm.data.db.cars.CarDAO
import ua.hospes.rtm.data.db.drivers.DriverDAO
import ua.hospes.rtm.data.db.sessions.SessionEntity
import ua.hospes.rtm.data.db.team.TeamDAO

data class SessionDto(
    val id: Long = 0,
    val teamId: Long,
    val driver: DriverDto? = null,
    val car: CarDto? = null,
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

private fun SessionEntity.Type.toDto(): SessionDto.Type = when (this) {
    SessionEntity.Type.TRACK -> SessionDto.Type.TRACK
    SessionEntity.Type.PIT -> SessionDto.Type.PIT
}

internal fun SessionDto.Type.toEntity(): SessionEntity.Type = when (this) {
    SessionDto.Type.TRACK -> SessionEntity.Type.TRACK
    SessionDto.Type.PIT -> SessionEntity.Type.PIT
}

internal suspend fun SessionEntity.toDto(
    teamDAO: TeamDAO,
    driverDAO: DriverDAO,
    carDAO: CarDAO,
): SessionDto = SessionDto(
    id = id,
    teamId = teamId,
    driver = driverId?.let { driverDAO.get(it).toDto(teamDAO) },
    car = carId?.let { carDAO.get(it).toDto() },
    raceStartTime = raceStartTime,
    startTime = startTime,
    endTime = endTime,
    type = type.toDto(),
)