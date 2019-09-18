package ua.hospes.rtm.domain.race.models

import android.util.SparseLongArray


/**
 * @author Andrew Khloponin
 */
class RaceItemDetails {
    var pitStops = 0
    private val completedDriversDuration = SparseLongArray()


    fun addDriverDuration(driverId: Long, duration: Long) {
        val oldDuration = getDriverDuration(driverId)
        completedDriversDuration.put(driverId.toInt(), oldDuration + duration)
    }

    fun getDriverDuration(driverId: Long): Long {
        val l = completedDriversDuration.get(driverId.toInt())
        return l ?: 0L
    }


    override fun toString(): String {
        return "RaceItemDetails{" +
                "pitStops=" + pitStops +
                ", completedDriversDuration=" + completedDriversDuration +
                '}'.toString()
    }
}