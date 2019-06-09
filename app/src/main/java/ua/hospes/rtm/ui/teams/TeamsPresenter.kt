package ua.hospes.rtm.ui.teams

import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.team.TeamsRepository
import ua.hospes.rtm.utils.RxUtils
import ua.hospes.rtm.utils.plusAssign
import javax.inject.Inject

class TeamsPresenter @Inject constructor(
        private val repo: TeamsRepository
) : Presenter<TeamsContract.View>() {


    override fun attachView(view: TeamsContract.View?) {
        super.attachView(view)
        disposables += repo.listen().compose(RxUtils.applySchedulers()).subscribe({ view?.onData(it) }, this::error)
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun removeAll() = launch {
        try {
            repo.removeAll().blockingAwait()
        } catch (t: Throwable) {
            error(t)
            return@launch
        }
    }
}