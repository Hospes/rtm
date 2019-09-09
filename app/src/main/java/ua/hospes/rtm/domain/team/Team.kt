package ua.hospes.rtm.domain.team

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ua.hospes.rtm.db.team.TeamEntity
import ua.hospes.rtm.domain.drivers.Driver

@Parcelize
data class Team(
        val id: Int = 0,
        val name: String,
        val drivers: MutableList<Driver> = mutableListOf()
) : Parcelable {

    fun setDrivers(vararg drivers: Driver) {
        this.drivers.clear()
        this.drivers.addAll(drivers)
    }

    fun setDrivers(drivers: Iterable<Driver>) {
        this.drivers.clear()
        this.drivers.addAll(drivers)
    }
}

fun Team.toDbEntity(): TeamEntity = TeamEntity(id, name)