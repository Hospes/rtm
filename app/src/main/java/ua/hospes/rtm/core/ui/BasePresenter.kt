package ua.hospes.rtm.core.ui

abstract class BasePresenter<V> {
    var view: V? = null
        private set

    open fun attachView(view: V) {
        this.view = view
    }

    open fun detachView() {
        this.view = null
    }
}