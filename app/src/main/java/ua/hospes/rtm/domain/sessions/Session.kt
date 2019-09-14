package ua.hospes.rtm.domain.sessions

import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.drivers.Driver

data class Session(
        val id: Int = 0,
        val teamId: Int,
        val driver: Driver? = null,
        val car: Car? = null,
        val raceStartTime: Long = -1L,
        val startDurationTime: Long = -1L,
        val endDurationTime: Long? = null,
        val type: Type = Type.TRACK
) {
    enum class Type {
        TRACK,
        PIT
    }
}