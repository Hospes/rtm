package ua.hospes.rtm.domain.team

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ua.hospes.rtm.data.model.DriverDto
import ua.hospes.rtm.data.model.TeamDto
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.drivers.toDomain
import ua.hospes.rtm.domain.drivers.toDto

@Parcelize
data class Team(
    val id: Long = 0,
    val name: String,
    val drivers: List<Driver> = emptyList()
) : Parcelable

internal fun TeamDto.toDomain(): Team = Team(id, name, drivers.map(DriverDto::toDomain))
internal fun Team.toDto(): TeamDto = TeamDto(id, name, drivers.map(Driver::toDto))