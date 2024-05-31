package ua.hospes.rtm.domain.sessions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.hospes.rtm.data.db.cars.CarDAO
import ua.hospes.rtm.data.db.drivers.DriverDAO
import ua.hospes.rtm.data.db.sessions.SessionEntity
import ua.hospes.rtm.data.db.team.TeamDAO
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.drivers.Driver

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

internal suspend fun SessionEntity.toDomain(teamDAO: TeamDAO, driverDAO: DriverDAO, carDAO: CarDAO): Session = withContext(Dispatchers.IO) {
    Session(
        id = id,
        teamId = teamId,
        driver = driverId?.let { driverDAO.get(it).toDomain(teamDAO) },
        car = carId?.let { carDAO.get(it).toDomain() },
        raceStartTime = raceStartTime,
        startTime = startTime,
        endTime = endTime,
        type = Session.Type.valueOf(type)
    )
}