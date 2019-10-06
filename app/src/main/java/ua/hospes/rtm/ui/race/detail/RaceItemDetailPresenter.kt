package ua.hospes.rtm.ui.race.detail

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.sessions.SessionsRepository
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
internal class RaceItemDetailPresenter @Inject constructor(
        private val raceRepo: RaceRepository,
        private val sessionRepo: SessionsRepository
) : Presenter<RaceItemDetailContract.View>() {

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun setRaceItemId(id: Long) {
        launch { raceRepo.listen(id).collect { view?.onRaceItem(it) } }
        launch { sessionRepo.listenByRaceId(id).collect { view?.onSessions(it) } }
    }
}