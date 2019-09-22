package ua.hospes.rtm.ui.race

import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.race.models.RaceItem

interface RaceContract {
    interface View {
        fun onData(items: List<RaceItem>)

        fun onOpenSetCarDialog(sessionId: Long, cars: List<Car>)
        fun onOpenSetDriverDialog(sessionId: Long, drivers: List<Driver>)

        fun onError(t: Throwable)
    }
}