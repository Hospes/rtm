package ua.hospes.rtm.domain.race.models

import ua.hospes.rtm.db.race.RaceEntity
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.team.Team

data class RaceItem(
        val id: Int = 0,
        val teamNumber: Int,
        val team: Team,
        val session: Session? = null,
        val details: RaceItemDetails? = null
)

fun RaceItem.toEntity(): RaceEntity = RaceEntity(id, team.id, teamNumber, session?.id)