package ua.hospes.rtm.domain.race.models

import ua.hospes.rtm.domain.sessions.Session

data class DriverDetails(
    var id: Long = -1,
    var name: String? = null,
    var prevDuration: Long = 0,
    var session: Session? = null
)