package ua.hospes.rtm.ui.teams

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.data.TeamsRepository
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.team.Team
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
internal class EditTeamPresenter /*@Inject*/ constructor(private val repo: TeamsRepository) : Presenter<EditTeamContract.View>() {
    private val initTeam = ConflatedBroadcastChannel<Team>()
    private val teamDrivers = ConflatedBroadcastChannel<List<Driver>>(emptyList())
    private val deleteButton = ConflatedBroadcastChannel<Boolean>(false)


    override fun attachView(view: EditTeamContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)
        launch { initTeam.consumeEach { t -> view?.onInitTeam(t).also { onDriversSelected(t.drivers) } } }
        launch { teamDrivers.consumeEach { view?.onSelectedDrivers(it) } }
        launch { deleteButton.consumeEach { view?.onDeleteButtonAvailable(it) } }
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun initTeam(team: Team?) = team?.let { initTeam.offer(it) }.let { Unit }


    fun clickSelectDrivers() = view?.onShowSelectDialog(teamDrivers.value) ?: Unit

    fun onDriversSelected(list: List<Driver>?) = teamDrivers.offer(list ?: emptyList()).let { Unit }


    fun save(name: CharSequence?) = launch {
        val team = Team(
                initTeam.valueOrNull?.id ?: 0,
                name?.toString() ?: return@launch,
                drivers = teamDrivers.value.toMutableList()
        )

        repo.save(team)

        view?.onSaved()
    }

    fun delete() = launch {
        repo.delete(initTeam.valueOrNull?.id ?: return@launch)

        view?.onDeleted()
    }
}