package ua.hospes.rtm.db.sessions

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SessionDb(
        val id: Int = 0,
        val teamId: Int,
        val driverId: Int? = null,
        val carId: Int? = null,
        val raceStartTime: Long = -1,
        val startDurationTime: Long = -1,
        val endDurationTime: Long = -1,
        val type: String? = null
) : Parcelable