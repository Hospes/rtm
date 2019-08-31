package ua.hospes.rtm.domain.race.models

import ua.hospes.rtm.domain.sessions.Session

/**
 * @author Andrew Khloponin
 */
class DriverDetails {
    //region Getters
    //endregion

    //region Setters
    var id = -1
    var name: String? = null
    var prevDuration = 0L
    var session: Session? = null
    //endregion
}