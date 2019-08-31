package ua.hospes.rtm.ui.race.detail

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.Dispatchers
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.sessions.SessionsRepository
import javax.inject.Inject

class RaceItemDetailPresenter @Inject constructor(
        private val raceRepo: RaceRepository,
        private val sessionRepo: SessionsRepository
) : Presenter<RaceItemDetailContract.View>(Dispatchers.Main) {

    override fun attachView(view: RaceItemDetailContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)


    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun listenRaceItemId(id: Int) {

    }

    private fun listenSessions(teamId: Int) {

    }

    //    internal fun listenRaceItem(raceItemId: Int) {
    //        RxUtils.manage(this, interactor.listenRaceItem(raceItemId)
    //                .compose(RxUtils.applySchedulers<T>())
    //                .subscribe({ raceItem ->
    //                    getView().onUpdateRaceItem(raceItem!!)
    //                    if (raceItem == null) return@interactor.listenRaceItem(raceItemId)
    //                            .compose(RxUtils.applySchedulers())
    //                            .subscribe
    //                    listenSessions(raceItem!!.getTeam().getId())
    //                }, ???({ printStackTrace() })))
    //    }
    //
    //    private fun listenSessions(teamId: Int) {
    //        RxUtils.manage(this, interactor.listenSessions(teamId)
    //                .compose(RxUtils.applySchedulers<T>())
    //                .subscribe({ sessions -> getView().onUpdateSessions(sessions) }, ???({ printStackTrace() })))
    //    }
}