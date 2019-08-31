package ua.hospes.rtm.domain.race.models

import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.team.Team

/**
 * @author Andrew Khloponin
 */
class RaceItem {
    //region Getters
    val id: Int
    val team: Team
    var teamNumber = -1
    //endregion

    //region Setters
    var session: Session? = null

    var details: RaceItemDetails? = null


    constructor(team: Team) {
        this.id = -1
        this.team = team
    }

    constructor(id: Int, team: Team) {
        this.id = id
        this.team = team
    }
    //endregion


    override fun toString(): String {
        return "RaceItem{" +
                "id=" + id +
                ", team=" + team +
                ", teamNumber=" + teamNumber +
                ", session=" + session +
                ", details=" + details +
                '}'.toString()
    }
}