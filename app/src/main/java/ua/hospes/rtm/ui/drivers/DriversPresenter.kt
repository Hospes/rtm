package ua.hospes.rtm.ui.drivers

import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.utils.RxUtils
import ua.hospes.rtm.utils.plusAssign
import javax.inject.Inject

class DriversPresenter @Inject constructor(
        private val repo: DriversRepository
) : Presenter<DriversContract.View>() {


    override fun attachView(view: DriversContract.View?) {
        super.attachView(view)
        disposables += repo.listen()
                .map { list -> list.sortedBy { it.name } }
                .compose(RxUtils.applySchedulers())
                .subscribe({ view?.onData(it) }, this::error)
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun removeAll() = launch {
        try {
            repo.removeAll().blockingAwait()
        } catch (t: Throwable) {
            error(t)
            return@launch
        }
    }
}