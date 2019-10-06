package ua.hospes.rtm.domain.race.models

import android.util.SparseLongArray

data class RaceItemDetails(
        val pitStops: Int = 0
) {
    private val completedDriversDuration = SparseLongArray()

    fun addDriverDuration(driverId: Long, duration: Long) =
            getDriverDuration(driverId).apply { completedDriversDuration.put(driverId.toInt(), this + duration) }.let { Unit }

    fun getDriverDuration(driverId: Long): Long = completedDriversDuration.get(driverId.toInt())
}