package ua.hospes.rtm.domain.sessions

import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.drivers.Driver

data class Session(
        val id: Long = 0,
        val teamId: Long,
        val driver: Driver? = null,
        val car: Car? = null,
        val raceStartTime: Long? = null,
        val startTime: Long? = null,
        val endTime: Long? = null,
        val type: Type = Type.TRACK
) {
    enum class Type {
        TRACK,
        PIT
    }
}