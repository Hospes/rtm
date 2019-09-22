package ua.hospes.rtm.ui.race

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.hospes.rtm.core.Presenter
import ua.hospes.rtm.domain.cars.CarsRepository
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.domain.race.RaceRepository
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.sessions.SessionsRepository
import javax.inject.Inject

internal class RacePresenter @Inject constructor(
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

    fun onPit(item: RaceItem, time: Long) = launch {
        val oldSessionId = item.session?.id ?: throw IllegalStateException("Race team doesn't have session to close")
        val raceStartTime = item.session.raceStartTime ?: throw IllegalStateException("Session doesn't have race start time")
        // Close old session
        sessionRepo.closeSessions(time, oldSessionId)
        // Open new session
        sessionRepo.startNewSession(item.team.id, raceStartTime, time, Session.Type.PIT)
    }

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