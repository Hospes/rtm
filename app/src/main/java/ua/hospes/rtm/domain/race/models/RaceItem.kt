package ua.hospes.rtm.domain.race.models

import ua.hospes.rtm.data.model.RaceDto
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.sessions.toDomain
import ua.hospes.rtm.domain.sessions.toDto
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.toDomain
import ua.hospes.rtm.domain.team.toDto

data class RaceItem(
    val id: Long = 0,
    val teamNumber: Int,
    val team: Team,
    val session: Session? = null,
    val details: RaceItemDetails? = null
)

internal fun RaceItem.toDto(): RaceDto = RaceDto(
    id = id,
    team = team.toDto(),
    teamNumber = teamNumber,
    session = session?.toDto(),
)

internal fun RaceDto.toDomain(): RaceItem = RaceItem(
    id = id,
    teamNumber = teamNumber,
    team = team.toDomain(),
    session = session?.toDomain(),
    details = details?.let {
        RaceItemDetails(pitStops = it.pitStops, completedDriversDuration = it.completedDriversDuration.clone())
    },
)
