package ua.hospes.rtm.ui.teams

import ua.hospes.rtm.domain.team.Team

interface TeamsContract {
    interface View {
        fun onData(list: List<Team>)

        fun onError(throwable: Throwable)
    }
}