package ua.hospes.rtm.ui.race

import androidx.lifecycle.Lifecycle
import io.reactivex.Observable
import io.reactivex.functions.Function3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.sessions.SessionsRepository
import ua.hospes.rtm.domain.team.Team
import ua.hospes.rtm.domain.team.TeamsRepository
import ua.hospes.rtm.utils.RxUtils
import ua.hospes.rtm.utils.plusAssign
import javax.inject.Inject

class AddTeamToRacePresenter @Inject constructor(
        private val raceRepo: RaceRepository,
        private val sessionsRepo: SessionsRepository,
        private val teamsRepo: TeamsRepository
) : Presenter<AddTeamToRaceContract.View>() {

    override fun attachView(view: AddTeamToRaceContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        disposables += teamsRepo.getNotInRace()
                .compose(RxUtils.applySchedulersSingle())
                .subscribe({ view?.onTeams(it) }, this::error)
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    @Suppress("ThrowableNotThrown")
    fun save(number: String, team: Team? = null) = launch(Dispatchers.IO) {
        val intNumber = try {
            number.toInt()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException(e)
        }

        team?.let {
            Observable.zip(
                    Observable.just(it),
                    Observable.just(intNumber),
                    sessionsRepo.newSessions(Session.Type.TRACK, it.id!!),
                    Function3 { team: Team, num: Int, session: Session ->
                        RaceItem(team).apply {
                            teamNumber = num
                            setSession(session)
                        }
                    }
            )
                    .flatMap { race -> raceRepo.addNew(race) }
                    .blockingSubscribe()
        } ?: throw IllegalArgumentException("Team not selected")

        withContext(Dispatchers.Main) { view?.onSuccess() }
        //                .compose(RxUtils.applySchedulers())
        //                .subscribe({ view?.onSuccess() }, this@AddTeamToRacePresenter::error)
    }
}