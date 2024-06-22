package ua.hospes.rtm.data.model

import ua.hospes.rtm.data.db.drivers.DriverEntity
import ua.hospes.rtm.data.db.team.TeamDAO
import ua.hospes.rtm.data.db.team.TeamEntity

data class DriverDto(
    val id: Long = 0,
    val name: String,
    val teamId: Long? = null,
    val teamName: String? = null
)

internal fun DriverEntity.toDto(team: TeamEntity? = null): DriverDto = team?.let {
    require(teamId == it.id) { "Wrong team" }
    DriverDto(id, name, it.id, it.name)
} ?: DriverDto(id, name, teamId)

internal suspend fun DriverEntity.toDto(dao: TeamDAO): DriverDto = teamId?.let {
    val team = dao.get(*longArrayOf(it)).firstOrNull() ?: throw IllegalStateException("Didn't found team with given id[$it]")
    DriverDto(id, name, team.id, team.name)
} ?: DriverDto(id, name)

internal fun DriverDto.toDbEntity(): DriverEntity = DriverEntity(id, name, teamId)