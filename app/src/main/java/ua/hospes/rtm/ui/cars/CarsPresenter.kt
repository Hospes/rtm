package ua.hospes.rtm.ui.cars

import androidx.lifecycle.Lifecycle
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.data.CarsRepository
import javax.inject.Inject

class CarsPresenter @Inject constructor(
        private val repo: CarsRepository
) : Presenter<CarsContract.View>() {

    override fun attachView(view: CarsContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        launch { repo.listen().collect { view?.onData(it) } }
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun removeAll() = launch { repo.clear() }
}