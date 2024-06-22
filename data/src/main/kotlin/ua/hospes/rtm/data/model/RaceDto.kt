package ua.hospes.rtm.data.model

import android.util.SparseLongArray
import ua.hospes.rtm.data.db.cars.CarDAO
import ua.hospes.rtm.data.db.drivers.DriverDAO
import ua.hospes.rtm.data.db.race.RaceEntity
import ua.hospes.rtm.data.db.sessions.SessionDAO
import ua.hospes.rtm.data.db.team.TeamDAO

data class RaceDto(
    val id: Long = 0,
    val teamNumber: Int,
    val team: TeamDto,
    val session: SessionDto? = null,
    val details: Details? = null
) {
    data class Details(
        val pitStops: Int = 0,
        val completedDriversDuration: SparseLongArray = SparseLongArray(),
    ) {
        internal fun addDriverDuration(driverId: Long, duration: Long) =
            getDriverDuration(driverId).apply { completedDriversDuration.put(driverId.toInt(), this + duration) }.let { }

        private fun getDriverDuration(driverId: Long): Long = completedDriversDuration.get(driverId.toInt())
    }
}

internal fun RaceDto.toEntity(): RaceEntity = RaceEntity(
    id = id,
    teamId = team.id,
    teamNumber = teamNumber,
    sessionId = session?.id
)

internal suspend fun RaceEntity.toDto(
    teamDAO: TeamDAO,
    driverDAO: DriverDAO,
    carDAO: CarDAO,
    sessionDAO: SessionDAO,
): RaceDto = RaceDto(
    id = id,
    teamNumber = teamNumber,
    team = teamDAO.get(teamId).toDto(driverDAO),
    session = sessionId?.let { sessionDAO.get(it) }?.toDto(teamDAO, driverDAO, carDAO),
    details = RaceDto.Details(sessionDAO.teamSessionsCount(teamId) - 1)
        .apply {
            sessionDAO.getByTeam(teamId).forEach {
                val duration = if (it.startTime == null || it.endTime == null) null else it.endTime - it.startTime
                if (it.driverId == null || duration == null) return@forEach
                addDriverDuration(it.driverId, duration)
            }
        }
)
