package ua.hospes.rtm.domain.drivers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ua.hospes.rtm.data.model.DriverDto

@Parcelize
data class Driver(
    val id: Long = 0,
    val name: String,
    val teamId: Long? = null,
    val teamName: String? = null
) : Parcelable

internal fun DriverDto.toDomain(): Driver = Driver(id, name, teamId, teamName)
internal fun Driver.toDto(): DriverDto = DriverDto(id, name, teamId, teamName)