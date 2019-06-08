package ua.hospes.rtm.ui.cars

import ua.hospes.rtm.domain.cars.Car

interface CarsContract {
    interface View {
        fun onData(cars: List<Car>)

        fun onError(throwable: Throwable)
    }
}