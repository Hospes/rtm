package ua.hospes.rtm.data.model

import ua.hospes.rtm.data.db.drivers.DriverDAO
import ua.hospes.rtm.data.db.team.TeamEntity

data class TeamDto(
    val id: Long = 0,
    val name: String,
    val drivers: List<DriverDto> = emptyList()
)

internal suspend fun TeamEntity.toDto(dao: DriverDAO): TeamDto =
    TeamDto(id, name, dao.getByTeamId(id).map { it.toDto(this@toDto) }.toMutableList())

internal fun TeamDto.toDbEntity(): TeamEntity = TeamEntity(id, name)