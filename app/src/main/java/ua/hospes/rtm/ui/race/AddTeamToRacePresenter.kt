package ua.hospes.rtm.ui.race

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.sessions.SessionsRepository
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.TeamsRepository
import javax.inject.Inject

internal class AddTeamToRacePresenter @Inject constructor(
        private val raceRepo: RaceRepository,
        private val sessionsRepo: SessionsRepository,
        private val teamsRepo: TeamsRepository
) : Presenter<AddTeamToRaceContract.View>() {

    override fun attachView(view: AddTeamToRaceContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        launch(Dispatchers.Main) { view?.onTeams(teamsRepo.getNotInRace()) }
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    @Suppress("ThrowableNotThrown")
    fun save(number: String, team: Team? = null) = launch(Dispatchers.Main) {
        val intNumber = number.toInt()

        team?.let {
            val session = sessionsRepo.newSession(Session.Type.TRACK, it.id)

            raceRepo.save(RaceItem(teamNumber = intNumber, team = it, session = session))
        } ?: throw IllegalArgumentException("Team not selected")

        view?.onSuccess()
    }
}