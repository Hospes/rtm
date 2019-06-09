package ua.hospes.rtm.domain.drivers

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Driver(
        val id: Int? = null,
        val name: String,
        val teamId: Int? = null,
        val teamName: String? = null
) : Parcelable