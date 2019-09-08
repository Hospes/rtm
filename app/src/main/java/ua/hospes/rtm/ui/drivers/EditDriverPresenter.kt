package ua.hospes.rtm.ui.drivers

import androidx.lifecycle.Lifecycle
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.TeamsRepository
import ua.hospes.rtm.utils.RxUtils
import ua.hospes.rtm.utils.plusAssign
import javax.inject.Inject

internal class EditDriverPresenter @Inject constructor(
        private val driversRepo: DriversRepository,
        private val teamsRepo: TeamsRepository
) : Presenter<EditDriverContract.View>() {
    private val initDriverSubject = BehaviorSubject.create<Driver>()
    private val deleteButtonSubject = BehaviorSubject.createDefault(false)


    override fun attachView(view: EditDriverContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        disposables += initDriverSubject.compose(RxUtils.applySchedulers()).subscribe {
            view?.onInitDriver(it)
            deleteButtonSubject.onNext(true)
        }

        launch(Dispatchers.Main) { teamsRepo.listen().collect { view?.onTeamsLoaded(it) } }

        launch(Dispatchers.Main) {
            val i = withContext(Dispatchers.IO) {
                initDriverSubject.value?.let { driver -> teamsRepo.get().indexOfFirst { it.id == driver.teamId } }
            }

            i?.let { view?.onTeamSelectionIndex(it) }
        }

        disposables += deleteButtonSubject.compose(RxUtils.applySchedulers()).subscribe { view?.onDeleteButtonAvailable(it) }
    }


    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun initDriver(driver: Driver?) = driver?.let { initDriverSubject.onNext(it) }

    fun save(name: CharSequence?, team: Team? = null) = launch {
        val driver = Driver(
                initDriverSubject.value?.id ?: 0,
                name?.toString() ?: return@launch,
                team?.id, team?.name
        )

        driversRepo.save(driver)

        withContext(Dispatchers.Main) { view?.onSaved() }
    }

    fun delete() = launch {
        driversRepo.delete(initDriverSubject.value?.id ?: return@launch)

        withContext(Dispatchers.Main) { view?.onDeleted() }
    }
}