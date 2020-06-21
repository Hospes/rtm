package ua.hospes.rtm.ui.race

import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.data.SessionsRepository
import javax.inject.Inject

class SetDriverPresenter @Inject constructor(
        private val sesRepo: SessionsRepository
) : Presenter<SetDriverContract.View>() {

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit

    fun setDriver(sessionId: Long, driverId: Long) = launch { sesRepo.setSessionDriver(sessionId, driverId) }
}