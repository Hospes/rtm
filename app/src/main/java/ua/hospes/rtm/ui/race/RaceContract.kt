package ua.hospes.rtm.ui.race

import ua.hospes.rtm.domain.race.models.RaceItem

interface RaceContract {
    interface View {
        fun onData(items: List<RaceItem>)

        fun onError(t: Throwable)
    }
}