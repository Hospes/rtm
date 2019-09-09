package ua.hospes.rtm.domain.drivers

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ua.hospes.rtm.db.drivers.DriverEntity

@Parcelize
data class Driver(
        val id: Int = 0,
        val name: String,
        val teamId: Int? = null,
        val teamName: String? = null
) : Parcelable

fun Driver.toDbEntity(): DriverEntity = DriverEntity(id, name, teamId)