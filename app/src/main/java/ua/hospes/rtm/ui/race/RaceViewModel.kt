package ua.hospes.rtm.ui.race

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.hospes.rtm.data.model.CarDto
import ua.hospes.rtm.data.model.DriverDto
import ua.hospes.rtm.data.model.RaceDto
import ua.hospes.rtm.data.model.SessionDto
import ua.hospes.rtm.data.repo.CarsRepository
import ua.hospes.rtm.data.repo.DriversRepository
import ua.hospes.rtm.data.repo.RaceRepository
import ua.hospes.rtm.data.repo.SessionsRepository
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.cars.toDomain
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.drivers.toDomain
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.race.models.toDomain
import javax.inject.Inject

@HiltViewModel
class RaceViewModel @Inject constructor(
    private val sessionRepo: SessionsRepository,
    private val driversRepo: DriversRepository,
    private val raceRepo: RaceRepository,
    private val carsRepo: CarsRepository
) : ViewModel() {

    val uiEvents = MutableLiveData<UIEvent>()
    val race = raceRepo.listen().map { it.map(RaceDto::toDomain) }.asLiveData()

    fun startRace(startTime: Long) = viewModelScope.launch { sessionRepo.startRace(startTime) }
    fun stopRace(stopTime: Long) = viewModelScope.launch { sessionRepo.stopRace(stopTime) }

    fun onPit(item: RaceItem, time: Long) =
        viewModelScope.launch { sessionRepo.closeCurrentStartNew(item.id, time, SessionDto.Type.PIT) }

    fun onOut(item: RaceItem, time: Long) =
        viewModelScope.launch { sessionRepo.closeCurrentStartNew(item.id, time, SessionDto.Type.TRACK) }

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

    fun resetRace() = viewModelScope.launch { sessionRepo.resetRace() }
    fun removeAll() = viewModelScope.launch { raceRepo.clear() }

    fun clickSetCar(item: RaceItem) = viewModelScope.launch {
        val id = item.session?.id ?: throw IllegalStateException("Race team doesn't have session")
        val items = carsRepo.getNotInRace().map(CarDto::toDomain)

        uiEvents.value = UIEvent.SetCar(id, items)
    }

    fun clickSetDriver(item: RaceItem) = viewModelScope.launch {
        val id = item.session?.id ?: throw IllegalStateException("Race team doesn't have session")
        val items = driversRepo.getNotInRace(item.team.id).map(DriverDto::toDomain)

        uiEvents.value = UIEvent.SetDriver(id, items)
    }

    sealed class UIEvent {
        data class SetCar(val id: Long, val cars: List<Car>) : UIEvent()
        data class SetDriver(val id: Long, val drivers: List<Driver>) : UIEvent()
    }
}