package ua.hospes.rtm.ui.race

import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.data.SessionsRepository
import javax.inject.Inject

class SetCarPresenter @Inject constructor(
        private val sesRepo: SessionsRepository
) : Presenter<SetCarContract.View>() {

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit

    fun setCar(sessionId: Long, carId: Long) = launch { sesRepo.setSessionCar(sessionId, carId) }
}