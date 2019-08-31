package ua.hospes.rtm.domain.sessions

import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.drivers.Driver

data class Session(val id: Int = -1,
                   val teamId: Int,
                   var driver: Driver? = null,
                   var car: Car? = null,
                   var raceStartTime: Long = -1L,
                   var startDurationTime: Long = -1L,
                   var endDurationTime: Long = -1L,
                   var type: Type = Type.TRACK
) {

    //private var type: Type? = Type.TRACK

    //    fun getType(): Type {
    //        return if (type == null) type = Type.TRACK else type
    //    }
    //
    //    fun setType(type: Type?) {
    //        this.type = type ?: Type.TRACK
    //    }
    //
    //    fun setType(type: String) {
    //        try {
    //            this.type = Type.valueOf(type)
    //        } catch (e: IllegalArgumentException) {
    //            this.type = Type.TRACK
    //        } catch (e: NullPointerException) {
    //            this.type = Type.TRACK
    //        }
    //
    //    }
    //endregion

    enum class Type(val title: String) {
        TRACK("ON TRACK"),
        PIT("PIT-STOP")
    }
}