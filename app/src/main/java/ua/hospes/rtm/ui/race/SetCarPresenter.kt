package ua.hospes.rtm.ui.race

import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.sessions.SessionsRepository
import javax.inject.Inject

internal class SetCarPresenter @Inject constructor(
        private val sessionsRepository: SessionsRepository
) : Presenter<SetCarContract.View>() {
    fun setCar(sessionId: Long, carId: Long) = launch {
        sessionsRepository.setSessionCar(sessionId, carId)
    }
}