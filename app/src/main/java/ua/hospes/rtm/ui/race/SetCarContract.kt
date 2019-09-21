package ua.hospes.rtm.ui.race

interface SetCarContract {
    interface View {
        fun onCarSet()
        fun onError(throwable: Throwable)
    }
}