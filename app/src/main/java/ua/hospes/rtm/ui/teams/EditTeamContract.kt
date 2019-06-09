package ua.hospes.rtm.ui.teams

import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team

interface EditTeamContract {
    interface View {

        fun onInitTeam(team: Team)
        fun onSelectedDrivers(list: List<Driver>)

        fun onShowSelectDialog(selected: List<Driver>)

        fun onDeleteButtonAvailable(available: Boolean)

        fun onSaved()
        fun onDeleted()

        fun onError(throwable: Throwable)
    }
}