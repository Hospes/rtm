package ua.hospes.rtm.ui.drivers

import androidx.lifecycle.Lifecycle
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
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


    override fun attachView(view: EditDriverContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        disposables += initDriverSubject.compose(RxUtils.applySchedulers()).subscribe {
            view?.onInitDriver(it)
            deleteButtonSubject.onNext(true)
        }

        disposables += teamsRepo.listen().compose(RxUtils.applySchedulers()).subscribe({ view?.onTeamsLoaded(it) }, this::error)

        disposables += Observable.zip(
                initDriverSubject, teamsRepo.get().toObservable(),
                BiFunction { driver: Driver, all: List<Team> -> all.indexOfFirst { it.id == driver.teamId } }
        )
                .compose(RxUtils.applySchedulers())
                .subscribe({ view?.onTeamSelectionIndex(it) }, this::error)


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