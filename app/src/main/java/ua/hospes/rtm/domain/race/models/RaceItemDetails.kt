package ua.hospes.rtm.domain.race.models

import android.util.SparseArray


/**
 * @author Andrew Khloponin
 */
class RaceItemDetails {
    var pitStops = 0
    private val completedDriversDuration = SparseArray<Long>()


    fun addDriverDuration(driverId: Int, duration: Long) {
        val oldDuration = getDriverDuration(driverId)
        completedDriversDuration.put(driverId, oldDuration + duration)
    }

    fun getDriverDuration(driverId: Int): Long {
        val l = completedDriversDuration.get(driverId)
        return l ?: 0L
    }


    override fun toString(): String {
        return "RaceItemDetails{" +
                "pitStops=" + pitStops +
                ", completedDriversDuration=" + completedDriversDuration +
                '}'.toString()
    }
}