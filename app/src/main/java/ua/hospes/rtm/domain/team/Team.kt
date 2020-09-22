package ua.hospes.rtm.domain.team

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ua.hospes.rtm.db.team.TeamEntity
import ua.hospes.rtm.domain.drivers.Driver

@Parcelize
data class Team(
        val id: Long = 0,
        val name: String,
        val drivers: List<Driver> = emptyList()
) : Parcelable

fun Team.toDbEntity(): TeamEntity = TeamEntity(id, name)