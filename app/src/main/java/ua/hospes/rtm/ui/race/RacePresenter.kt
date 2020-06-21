package ua.hospes.rtm.ui.race

import androidx.lifecycle.Lifecycle
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.data.CarsRepository
import ua.hospes.rtm.data.DriversRepository
import ua.hospes.rtm.data.RaceRepository
import ua.hospes.rtm.data.SessionsRepository
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import javax.inject.Inject

@ActivityRetainedScoped
class RacePresenter @Inject constructor(
        private val raceRepo: RaceRepository,
        private val sessionRepo: SessionsRepository,
        private val carsRepo: CarsRepository,
        private val driversRepo: DriversRepository
) : Presenter<RaceContract.View>() {

    override fun attachView(view: RaceContract.View?, lc: Lifecycle) {
        super.attachView(view, lc)

        launch(Dispatchers.Main) { raceRepo.listen().collect { view?.onData(it) } }
    }

    override fun onError(throwable: Throwable) = view?.onError(throwable) ?: Unit
    override fun onUnexpectedError(throwable: Throwable) = view?.onError(throwable) ?: Unit


    fun startRace(startTime: Long) = launch { sessionRepo.startRace(startTime) }
    fun stopRace(stopTime: Long) = launch { sessionRepo.stopRace(stopTime) }

    fun onPit(item: RaceItem, time: Long) = launch { sessionRepo.closeCurrentStartNew(item.id, time, Session.Type.PIT) }
    fun onOut(item: RaceItem, time: Long) = launch { sessionRepo.closeCurrentStartNew(item.id, time, Session.Type.TRACK) }

    //    fun teamPit(item: RaceItem, time: Long): Observable<Boolean> {
    //        var driverId = -1
    //        var raceStartTime: Long = -1
    //        if (item.session != null) raceStartTime = item.session!!.raceStartTime
    //        when (preferencesManager.pitStopAssign) {
    //            PitStopAssign.PREVIOUS -> if (item.session != null && item.session!!.driver != null)
    //                driverId = item.session!!.driver!!.id!!
    //        }
    //        return Observable.zip<Session, Session, RaceItem>(
    //                closeSession(time, item.session),
    //                sessionsRepository.startNewSession(raceStartTime, time, Session.Type.PIT, driverId, item.team.id!!),
    //                { closedSession, openedSession ->
    //                    item.session = openedSession
    //                    item
    //                })
    //                .toList()
    //                .flatMapObservable(Function<List<RaceItem>, ObservableSource<out Boolean>> { raceRepository.update(it) })
    //    }

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

    fun resetRace() = launch { sessionRepo.resetRace() }
    fun removeAll() = launch { raceRepo.clear() }

    fun clickSetCar(item: RaceItem) = launch(Dispatchers.Main) {
        val id = item.session?.id ?: throw IllegalStateException("Race team doesn't have session")
        val items = carsRepo.getNotInRace()

        view?.onOpenSetCarDialog(id, items)
    }

    fun clickSetDriver(item: RaceItem) = launch(Dispatchers.Main) {
        val id = item.session?.id ?: throw IllegalStateException("Race team doesn't have session")
        val items = driversRepo.getNotInRace(item.team.id)

        view?.onOpenSetDriverDialog(id, items)
    }
}