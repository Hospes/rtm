package ua.hospes.rtm.ui.drivers

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.data.DriversRepository
import ua.hospes.rtm.data.TeamsRepository
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
class EditDriverPresenter @Inject constructor(
        private val driversRepo: DriversRepository,
        private val teamsRepo: TeamsRepository
) : Presenter<EditDriverContract.View>(Dispatchers.Main) {
    private val initDriver = ConflatedBroadcastChannel<Driver>()
    private val deleteButton = ConflatedBroadcastChannel<Boolean>(false)


    override fun attachView(view: EditDriverContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        launch { initDriver.consumeEach { view?.onInitDriver(it).also { deleteButton.offer(true) } } }
        launch { deleteButton.consumeEach { view?.onDeleteButtonAvailable(it) } }

        launch { teamsRepo.listen().collect { view?.onTeamsLoaded(it) } }

        launch {
            val i = withContext(Dispatchers.IO) {
                initDriver.valueOrNull?.let { driver -> teamsRepo.get().indexOfFirst { it.id == driver.teamId } }
            }

            i?.let { view?.onTeamSelectionIndex(it) }
        }
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun initDriver(driver: Driver?) = driver?.let { initDriver.offer(it) }.let { Unit }

    fun save(name: CharSequence?, team: Team? = null) = launch {
        val driver = Driver(
                initDriver.valueOrNull?.id ?: 0,
                name?.toString() ?: return@launch,
                team?.id, team?.name
        )

        driversRepo.save(driver)

        view?.onSaved()
    }

    fun delete() = launch {
        driversRepo.delete(initDriver.valueOrNull?.id ?: return@launch)

        view?.onDeleted()
    }
}