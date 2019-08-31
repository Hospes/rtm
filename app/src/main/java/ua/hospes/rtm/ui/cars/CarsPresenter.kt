package ua.hospes.rtm.ui.cars

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.cars.CarsRepository
import ua.hospes.rtm.utils.RxUtils
import ua.hospes.rtm.utils.plusAssign
import javax.inject.Inject

class CarsPresenter @Inject constructor(
        private val repo: CarsRepository
) : Presenter<CarsContract.View>() {

    override fun attachView(view: CarsContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        disposables += repo.listen()
                .compose(RxUtils.applySchedulers())
                .subscribe({ view?.onData(it) }, this::error)
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun removeAll() = launch { repo.removeAll().blockingAwait() }
}