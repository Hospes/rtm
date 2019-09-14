package ua.hospes.rtm.ui.teams

import androidx.lifecycle.Lifecycle
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.TeamsRepository
import ua.hospes.rtm.utils.RxUtils
import ua.hospes.rtm.utils.plusAssign
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
internal class EditTeamPresenter @Inject constructor(private val repo: TeamsRepository) : Presenter<EditTeamContract.View>() {
    private val initTeamSubject = BehaviorSubject.create<Team>()
    private val teamDrivers = ConflatedBroadcastChannel(emptyList<Driver>())
    private val deleteButtonSubject = BehaviorSubject.createDefault(false)


    override fun attachView(view: EditTeamContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)
        disposables += initTeamSubject.compose(RxUtils.applySchedulers()).subscribe {
            view?.onInitTeam(it)
            onDriversSelected(it.drivers)
        }

        launch(Dispatchers.Main) { teamDrivers.consumeEach { view?.onSelectedDrivers(it) } }
        disposables += deleteButtonSubject.compose(RxUtils.applySchedulers()).subscribe { view?.onDeleteButtonAvailable(it) }
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun initTeam(team: Team?) = team?.let { initTeamSubject.onNext(it) }


    fun clickSelectDrivers() = view?.onShowSelectDialog(teamDrivers.value)

    fun onDriversSelected(list: List<Driver>?) = teamDrivers.offer(list ?: emptyList()).let { Unit }


    fun save(name: CharSequence?) = launch {
        val team = Team(
                initTeamSubject.value?.id ?: 0,
                name?.toString() ?: return@launch,
                drivers = teamDrivers.value.toMutableList()
        )

        repo.save(team)

        withContext(Dispatchers.Main) { view?.onSaved() }
    }

    fun delete() = launch {
        repo.delete(initTeamSubject.value?.id ?: return@launch)

        withContext(Dispatchers.Main) { view?.onDeleted() }
    }
}