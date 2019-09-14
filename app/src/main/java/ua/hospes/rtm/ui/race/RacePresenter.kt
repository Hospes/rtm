package ua.hospes.rtm.ui.race

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.sessions.SessionsRepository
import javax.inject.Inject

internal class RacePresenter @Inject constructor(
        private val raceRepo: RaceRepository,
        private val sessionRepo: SessionsRepository
) : Presenter<RaceContract.View>() {

    override fun attachView(view: RaceContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        launch(Dispatchers.Main) { raceRepo.listen().collect { view?.onData(it) } }
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit



    fun startRace(startTime: Long) = launch {
        // Legacy way
        //val sessionIds = raceRepo.get().filterNot { it.session == null }.map { it.id }
        //sessionRepo.startSessions(startTime, startTime, *sessionIds.toIntArray())

        // New way
        sessionRepo.startRace(startTime)
    }

    fun stopRace(stopTime: Long) = launch {
        // Legacy way
        //val sessionIds = raceRepo.get().filterNot { it.session == null }.map { it.id }
        //sessionRepo.closeSessions(stopTime, *sessionIds.toIntArray())

        // New way
        sessionRepo.stopRace(stopTime)
    }

    fun onPit(item: RaceItem, time: Long) {
        //        disposables += interactor.teamPit(item, time)
        //                .compose(RxUtils.applySchedulers())
        //                .subscribe({ }, this::error)
    }

    fun onOut(item: RaceItem, time: Long) {
        //        disposables += interactor.teamOut(item, time)
        //                .compose(RxUtils.applySchedulers())
        //                .subscribe({ }, this::error)
    }

    fun undoLastSession(item: RaceItem) {
        //        disposables += interactor.removeLastSession(item)
        //                .compose(RxUtils.applySchedulers())
        //                .subscribe({ }, this::error)
    }

    fun exportXLS() {
        //        disposables += interactor.exportXLS()
        //                .compose(RxUtils.applySchedulersSingle())
        //                .subscribe({
        //                    result -> Toast.makeText(view!!.getContext(), "File located at: " + result.getAbsolutePath(), Toast.LENGTH_LONG).show()
        //                }, this::error)
    }

    fun resetRace() {
        //        disposables += interactor.resetRace()
        //                .compose(RxUtils.applySchedulers())
        //                .subscribe({ }, this::error)
    }

    fun removeAll() {
        //        disposables += interactor.removeAll()
        //                .compose(RxUtils.applySchedulersSingle())
        //                .subscribe({ }, this::error)
    }

    fun showRaceItemDetail(context: Context, item: RaceItem) {
        //RaceItemDetailActivity.start(context, item.id)
    }

    fun showSetCarDialog(managerFragment: FragmentManager, session: Session) {
        //        disposables += interactor.carsNotInRace.toList()
        //                .compose(RxUtils.applySchedulersSingle())
        //                .subscribe({ }, this::error)
        //.subscribe({ result -> SetCarDialogFragment.newInstance(session.id, result).show(managerFragment, "set_car") }, Consumer<Throwable> { it.printStackTrace() }))
    }

    fun showSetDriverDialog(managerFragment: FragmentManager, session: Session) {
        //        disposables += interactor.getDrivers(session.teamId)
        //                .compose(RxUtils.applySchedulersSingle())
        //                .subscribe({ }, this::error)
        //.subscribe({ result -> SetDriverDialogFragment.newInstance(session.id, session.teamId, result).show(managerFragment, "set_driver") }, Consumer<Throwable> { it.printStackTrace() }))
    }
}