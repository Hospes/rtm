package ua.hospes.rtm.ui.race

import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.race.models.RaceItem

interface RaceContract {
    interface View {
        fun onData(items: List<RaceItem>)

        fun onOpenSetCarDialog(sessionId: Long, cars: List<Car>)

        fun onError(t: Throwable)
    }
}