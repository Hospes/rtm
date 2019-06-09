package ua.hospes.rtm.ui.drivers

import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team

interface EditDriverContract {
    interface View {
        fun onInitDriver(driver: Driver)
        fun onTeamSelectionIndex(index: Int)
        fun onTeamsLoaded(teams: List<Team>)

        fun onDeleteButtonAvailable(available: Boolean)

        fun onSaved()
        fun onDeleted()

        fun onError(throwable: Throwable)
    }
}