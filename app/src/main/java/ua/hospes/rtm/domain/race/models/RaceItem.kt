package ua.hospes.rtm.domain.race.models

import ua.hospes.rtm.data.db.cars.CarDAO
import ua.hospes.rtm.data.db.drivers.DriverDAO
import ua.hospes.rtm.data.db.race.RaceEntity
import ua.hospes.rtm.data.db.sessions.SessionDAO
import ua.hospes.rtm.data.db.sessions.toDomain
import ua.hospes.rtm.data.db.team.TeamDAO
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.team.Team

data class RaceItem(
    val id: Long = 0,
    val teamNumber: Int,
    val team: Team,
    val session: Session? = null,
    val details: RaceItemDetails? = null
)

fun RaceItem.toEntity(): RaceEntity = RaceEntity(
    id = id,
    teamId = team.id,
    teamNumber = teamNumber,
    sessionId = session?.id
)

internal suspend fun RaceEntity.toDomain(
    teamDAO: TeamDAO,
    driverDAO: DriverDAO,
    carDAO: CarDAO,
    sessionDAO: SessionDAO,
): RaceItem = RaceItem(
    id = id,
    teamNumber = teamNumber,
    team = teamDAO.get(teamId).toDomain(driverDAO),
    session = sessionId?.let { sessionDAO.get(it) }?.toDomain(teamDAO, driverDAO, carDAO),
    details = RaceItemDetails(sessionDAO.teamSessionsCount(teamId) - 1)
        .apply {
            sessionDAO.getByTeam(teamId).forEach {
                val duration = if (it.startTime == null || it.endTime == null) null else it.endTime - it.startTime
                if (it.driverId == null || duration == null) return@forEach
                addDriverDuration(it.driverId, duration)
            }
        }
)
