package ua.hospes.rtm.data.sessions.models

import android.content.ContentValues

import ua.hospes.dbhelper.IDbModel
import ua.hospes.rtm.core.db.tables.Sessions

/**
 * @author Andrew Khloponin
 */
class SessionDb(val teamId: Int) : IDbModel {
    //region Getters
    //endregion

    //region Setters
    var id = -1
    var driverId = -1
    var carId = -1
    var raceStartTime: Long = -1
    var startDurationTime: Long = -1
    var endDurationTime: Long = -1
    var type: String? = null
    //endregion


    override fun toString(): String {
        return "SessionDb{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", driverId=" + driverId +
                ", carId=" + carId +
                ", raceStartTime=" + raceStartTime +
                ", startDurationTime=" + startDurationTime +
                ", endDurationTime=" + endDurationTime +
                ", type='" + type + '\''.toString() +
                '}'.toString()
    }

    override fun toContentValues(): ContentValues {
        val cv = ContentValues()

        cv.put(Sessions.TEAM_ID.name(), teamId)
        cv.put(Sessions.DRIVER_ID.name(), driverId)
        cv.put(Sessions.CAR_ID.name(), carId)
        cv.put(Sessions.RACE_START_TIME.name(), raceStartTime)
        cv.put(Sessions.START_DURATION_TIME.name(), startDurationTime)
        cv.put(Sessions.END_DURATION_TIME.name(), endDurationTime)
        cv.put(Sessions.TYPE.name(), type)

        return cv
    }
}