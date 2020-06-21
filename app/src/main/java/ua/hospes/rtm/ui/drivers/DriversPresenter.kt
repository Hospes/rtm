package ua.hospes.rtm.ui.drivers

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.data.DriversRepository
import javax.inject.Inject

class DriversPresenter @Inject constructor(private val repo: DriversRepository) : Presenter<DriversContract.View>() {

    override fun attachView(view: DriversContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        launch(Dispatchers.Main) { repo.listen().collect { view?.onData(it) } }
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun removeAll() = launch { repo.clear() }
}