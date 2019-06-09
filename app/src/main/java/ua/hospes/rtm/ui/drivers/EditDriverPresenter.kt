package ua.hospes.rtm.ui.drivers

import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
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

class EditDriverPresenter @Inject constructor(
        private val driversRepo: DriversRepository,
        private val teamsRepo: TeamsRepository
) : Presenter<EditDriverContract.View>() {
    private val initDriverSubject = BehaviorSubject.create<Driver>()
    private val deleteButtonSubject = BehaviorSubject.createDefault(false)


    override fun attachView(view: EditDriverContract.View?) {
        super.attachView(view)

        disposables += initDriverSubject.compose(RxUtils.applySchedulers()).subscribe {
            view?.onInitDriver(it)
            deleteButtonSubject.onNext(true)
        }

        disposables += teamsRepo.get()
                .compose(RxUtils.applySchedulersSingle())
                .subscribe({
                    view?.onTeamsLoaded(it)
                    //                    teams.add(team)
                    //                    adapter.add(team.getName())
                    //                    if (driver != null && driver.getTeamId() == team.getId()) {
                    //                        spinner.setSelection(teams.size)
                    //                    }
                }, this::error)


        disposables += deleteButtonSubject.compose(RxUtils.applySchedulers()).subscribe { view?.onDeleteButtonAvailable(it) }
    }


    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun initDriver(driver: Driver?) = driver?.let { initDriverSubject.onNext(it) }

    fun save(name: CharSequence?, team: Team? = null) = launch {
        val driver = Driver(
                initDriverSubject.value?.id,
                name?.toString() ?: return@launch,
                team?.id, team?.name
        )

        try {
            withContext(Dispatchers.IO) { driversRepo.save(driver).blockingAwait() }
        } catch (t: Throwable) {
            error(t)
            return@launch
        }

        withContext(Dispatchers.Main) { view?.onSaved() }
    }

    fun delete() = launch {
        val id = initDriverSubject.value?.id ?: return@launch

        try {
            withContext(Dispatchers.IO) { driversRepo.remove(id).blockingAwait() }
        } catch (t: Throwable) {
            error(t)
            return@launch
        }

        withContext(Dispatchers.Main) { view?.onDeleted() }
    }
}