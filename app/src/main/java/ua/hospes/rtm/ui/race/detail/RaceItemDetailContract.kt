package ua.hospes.rtm.ui.race.detail

import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session

interface RaceItemDetailContract {
    interface View {
        fun onUpdateRaceItem(item: RaceItem)
        fun onUpdateSessions(list: List<Session>)

        fun onError(t: Throwable)
    }
}