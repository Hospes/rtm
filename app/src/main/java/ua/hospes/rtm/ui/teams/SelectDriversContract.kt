package ua.hospes.rtm.ui.teams

import ua.hospes.rtm.domain.drivers.Driver

interface SelectDriversContract {
    interface View {
        fun onDrivers(list: List<Driver>)

        fun onSaveSelectedDrivers(list: List<Driver>)

        fun onError(throwable: Throwable)
    }
}