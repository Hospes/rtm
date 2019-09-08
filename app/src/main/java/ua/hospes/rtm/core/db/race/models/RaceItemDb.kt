package ua.hospes.rtm.core.db.race.models

import android.content.ContentValues

import ua.hospes.dbhelper.IDbModel
import ua.hospes.rtm.core.db.tables.Race

/**
 * @author Andrew Khloponin
 */
class RaceItemDb(val teamId: Int) : IDbModel {
    //region Getters
    //endregion

    //region Setters
    var id = -1
    var teamNumber = -1
    var sessionId = -1
    var order = 0
    //endregion


    override fun toString(): String {
        return "RaceItemDb{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", teamNumber=" + teamNumber +
                ", sessionId=" + sessionId +
                ", order=" + order +
                '}'.toString()
    }

    override fun toContentValues(): ContentValues {
        val cv = ContentValues()

        cv.put(Race.TEAM_ID.name(), teamId)
        cv.put(Race.TEAM_NUMBER.name(), teamNumber)
        cv.put(Race.SESSION_ID.name(), sessionId)

        return cv
    }
}