package ua.hospes.rtm.ui.teams

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.drivers.DriversRepository
import javax.inject.Inject

internal class SelectDriversPresenter @Inject constructor(
        private val repo: DriversRepository
) : Presenter<SelectDriversContract.View>() {

    override fun attachView(view: SelectDriversContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        //        disposables += repo.listen()
        //                .map { list -> list.sortedBy { it.name } }
        //                .map { list -> list.sortedBy { it.teamName } }
        //                .compose(RxUtils.applySchedulers())
        //                .subscribe({ view?.onDrivers(it) }, this::error)
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun save(selectedIds: List<Int>) = launch {
        //        val drivers = withContext(Dispatchers.IO) { repo.get().blockingGet() }

        //        val result = drivers.filter { selectedIds.contains(it.id) }

        //        withContext(Dispatchers.Main) { view?.onSaveSelectedDrivers(result) }
    }
}