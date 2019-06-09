package ua.hospes.rtm.ui.drivers

import ua.hospes.rtm.domain.drivers.Driver

interface DriversContract {
    interface View {
        fun onData(list: List<Driver>)

        fun onError(throwable: Throwable)
    }
}