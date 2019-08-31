package ua.hospes.rtm.domain.sessions

import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.drivers.Driver

class Session {
    //region Getters
    val id: Int
    val teamId: Int
    //endregion

    //region Setters
    var driver: Driver? = null
    var car: Car? = null
    var raceStartTime: Long = -1
    var startDurationTime: Long = -1
    var endDurationTime: Long = -1
    private var type: Type? = Type.TRACK


    constructor(teamId: Int) {
        this.id = -1
        this.teamId = teamId
    }

    constructor(id: Int, teamId: Int) {
        this.id = id
        this.teamId = teamId
    }

    fun getType(): Type {
        return if (type == null) type = Type.TRACK else type
    }

    fun setType(type: Type?) {
        this.type = type ?: Type.TRACK
    }

    fun setType(type: String) {
        try {
            this.type = Type.valueOf(type)
        } catch (e: IllegalArgumentException) {
            this.type = Type.TRACK
        } catch (e: NullPointerException) {
            this.type = Type.TRACK
        }

    }
    //endregion


    override fun toString(): String {
        return "Session{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", driver=" + driver +
                ", car=" + car +
                ", raceStartTime=" + raceStartTime +
                ", startDurationTime=" + startDurationTime +
                ", endDurationTime=" + endDurationTime +
                ", type=" + type +
                '}'.toString()
    }


    enum class Type private constructor(val title: String) {
        TRACK("ON TRACK"),
        PIT("PIT-STOP")
    }
}