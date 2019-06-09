package ua.hospes.rtm.ui.teams

import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.TeamsRepository
import ua.hospes.rtm.utils.RxUtils
import ua.hospes.rtm.utils.plusAssign
import javax.inject.Inject

class EditTeamPresenter @Inject constructor(
        private val repo: TeamsRepository
) : Presenter<EditTeamContract.View>() {
    private val initTeamSubject = BehaviorSubject.create<Team>()
    private val teamDriversSubject = BehaviorSubject.createDefault(emptyList<Driver>())
    private val deleteButtonSubject = BehaviorSubject.createDefault(false)


    override fun attachView(view: EditTeamContract.View?) {
        super.attachView(view)
        disposables += initTeamSubject.compose(RxUtils.applySchedulers()).subscribe {
            view?.onInitTeam(it)
            onDriversSelected(it.drivers)
        }
        disposables += teamDriversSubject.compose(RxUtils.applySchedulers()).subscribe { view?.onSelectedDrivers(it) }
        disposables += deleteButtonSubject.compose(RxUtils.applySchedulers()).subscribe { view?.onDeleteButtonAvailable(it) }
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun initTeam(team: Team?) = team?.let { initTeamSubject.onNext(it) }


    fun clickSelectDrivers() = view?.onShowSelectDialog(teamDriversSubject.value ?: emptyList())

    fun onDriversSelected(list: List<Driver>?) = teamDriversSubject.onNext(list ?: emptyList())


    fun save(name: CharSequence?) = launch {
        val team = Team(
                initTeamSubject.value?.id,
                name?.toString() ?: return@launch,
                drivers = (teamDriversSubject.value ?: emptyList()).toMutableList()
        )

        try {
            withContext(Dispatchers.IO) { repo.save(team).blockingAwait() }
        } catch (t: Throwable) {
            error(t)
            return@launch
        }

        withContext(Dispatchers.Main) { view?.onSaved() }
    }

    fun delete() = launch {
        val id = initTeamSubject.value?.id ?: return@launch

        try {
            withContext(Dispatchers.IO) { repo.remove(id).blockingAwait() }
        } catch (t: Throwable) {
            error(t)
            return@launch
        }

        withContext(Dispatchers.Main) { view?.onDeleted() }
    }
}