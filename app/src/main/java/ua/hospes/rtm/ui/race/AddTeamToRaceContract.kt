package ua.hospes.rtm.ui.race

import ua.hospes.rtm.domain.team.Team

interface AddTeamToRaceContract {
    interface View {
        fun onTeams(list: List<Team>)
        fun onSuccess()
        fun onError(throwable: Throwable)
    }
}