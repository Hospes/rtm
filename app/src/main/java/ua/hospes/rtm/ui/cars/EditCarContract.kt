package ua.hospes.rtm.ui.cars

import ua.hospes.rtm.domain.cars.Car

interface EditCarContract {
    interface View {
        fun onInitCar(car: Car)

        fun onDeleteButtonAvailable(available: Boolean)

        fun onSaved()
        fun onDeleted()

        fun onError(throwable: Throwable)
    }
}