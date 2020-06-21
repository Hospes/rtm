package ua.hospes.rtm.ui.teams

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.data.DriversRepository
import javax.inject.Inject

class SelectDriversPresenter @Inject constructor(
        private val repo: DriversRepository
) : Presenter<SelectDriversContract.View>() {

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun attachView(view: SelectDriversContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        launch(Dispatchers.Main) {
            repo.listen()
                    .map { list -> list.sortedBy { it.name }.sortedBy { it.teamName } }
                    .flowOn(Dispatchers.IO)
                    .collect { view?.onDrivers(it) }
        }
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun save(selectedIds: List<Long>) = launch(Dispatchers.Main) {
        val drivers = withContext(Dispatchers.IO) { repo.get(*selectedIds.toLongArray()) }

        view?.onSaveSelectedDrivers(drivers)
    }
}