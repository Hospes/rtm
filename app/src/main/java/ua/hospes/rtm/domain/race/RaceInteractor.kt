package ua.hospes.rtm.domain.race

import io.reactivex.Observable
import io.reactivex.Single
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.cars.CarsRepository
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.domain.preferences.PreferencesManager
import ua.hospes.rtm.domain.race.models.RaceItem
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.sessions.SessionsRepository
import ua.hospes.rtm.utils.Optional
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RaceInteractor @Inject constructor(
        private val preferencesManager: PreferencesManager,
        private val raceRepository: RaceRepository,
        private val sessionsRepository: SessionsRepository,
        private val driversRepository: DriversRepository,
        private val carsRepository: CarsRepository) {

    val carsNotInRace: Observable<Car>
        get() = carsRepository.getNotInRace()


    fun listen(): Observable<List<RaceItem>> {
        return raceRepository.listen()
    }

    fun setDriver(sessionId: Int, teamId: Int, driverId: Int): Observable<Boolean> {
        return Observable.empty()
        //        when (preferencesManager.pitStopAssign) {
        //            PitStopAssign.NEXT -> return Observable.zip(
        //                    setDriverForLatestPitSession(teamId, driverId),
        //                    sessionsRepository.setSessionDriver(sessionId, driverId),
        //                    { session, session2 -> true }
        //            )
        //            else -> return sessionsRepository.setSessionDriver(sessionId, driverId).map { session -> true }
        //        }
    }

    fun setCar(sessionId: Int, car: Car): Observable<Boolean> = sessionsRepository.setSessionCar(sessionId, car.id!!).map { true }

    fun startRace(startTime: Long): Single<Boolean> = raceRepository.get()
            .filter { item -> item.session != null }
            .map { item -> item.session!!.id }.toList()
            .map { it.toIntArray() }
            .flatMap { sessionIds -> sessionsRepository.startSessions(startTime, startTime, *sessionIds).toList() }
            .map { true }

    fun stopRace(stopTime: Long): Single<Boolean> = raceRepository.get()
            .filter { item -> item.session != null }
            .map { item -> item.session!!.id }.toList()
            .map { it.toIntArray() }
            .flatMap { teamIds -> sessionsRepository.closeSessions(stopTime, *teamIds).toList() }
            .map { true }

    fun teamPit(item: RaceItem, time: Long): Observable<Boolean> {
        return Observable.empty()
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
    }

    fun teamOut(item: RaceItem, time: Long): Observable<Boolean> {
        return Observable.empty()
        //        var raceStartTime: Long = -1
        //        if (item.session != null) raceStartTime = item.session!!.raceStartTime
        //        return Observable.zip<Session, Session, RaceItem>(
        //                closeSession(time, item.session),
        //                sessionsRepository.startNewSessions(raceStartTime, time, Session.Type.TRACK, item.team.id),
        //                { closedSession, openedSession ->
        //                    item.session = openedSession
        //                    item
        //                })
        //                .toList()
        //                .flatMapObservable(Function<List<RaceItem>, ObservableSource<out Boolean>> { raceRepository.update(it) })
    }

    fun removeLastSession(raceItem: RaceItem): Observable<Boolean> {
        return Observable.empty()
        //        return Observable.zip<Session, RaceItem, RaceItem>(sessionsRepository.removeLastSession(raceItem.team.id!!), Observable.just(raceItem), BiFunction<Session, RaceItem, RaceItem> { session, item -> this.updateRaceItemSession(session, item) })
        //                .toList()
        //                .flatMapObservable(Function<List<RaceItem>, ObservableSource<out Boolean>> { raceRepository.update(it) })
    }

    private fun updateRaceItemSession(session: Session, item: RaceItem): RaceItem {
        item.session = session
        return item
    }


    fun getDrivers(teamId: Int): Single<List<Driver>> {
        return driversRepository.getByTeamId(teamId)
    }

    fun resetRace(): Observable<Optional<*>> {
        return Observable.empty()
        //        return sessionsRepository.removeAll()
        //                .flatMap { count -> raceRepository.get().toList() }
        //                .flatMap { raceItems ->
        //                    Observable.zip<RaceItem, Session, RaceItem>(
        //                            Observable.fromIterable(raceItems),
        //                            sessionsRepository.newSessions(Session.Type.TRACK, *Ints.toArray(Collections2.transform(raceItems) { input -> input.team.id })),
        //                            { item, session ->
        //                                item.session = session
        //                                item
        //                            }).toList()
        //                }
        //                .flatMapObservable<Boolean>(Function<List<RaceItem>, ObservableSource<out Boolean>> { raceRepository.update(it) })
        //                .map { list -> Optional.empty<Any>() }
    }

    fun removeAll(): Single<Void> {
        return Single.error(RuntimeException())
        //        return Single.zip(raceRepository.removeAll(), sessionsRepository.removeAll(), { aVoid, aVoid2 -> null })
    }


    fun exportXLS(): Single<File> {
        return Single.error(RuntimeException())
        //        return raceRepository.get()
        //                .map<Team>(Function<RaceItem, Team> { it.getTeam() })
        //                .flatMap { team -> Observable.zip<Team, List<Session>, Pair<Team, List<Session>>>(Observable.just(team), getTeamSessions(team.id!!), BiFunction<Team, List<Session>, Pair<Team, List<Session>>> { first, second -> Pair(first, second) }) }
        //                .toList().map<Map<Team, List<Session>>> { pairs ->
        //                    val data = HashMap<Team, List<Session>>()
        //                    for (pair in pairs) {
        //                        data[pair.first] = pair.second
        //                    }
        //                    data
        //                }
        //                .flatMap { teams ->
        //                    Single.create<File> { subscriber ->
        //                        try {
        //                            subscriber.onSuccess(XLSTest.createWorkbook(teams))
        //                        } catch (e: IOException) {
        //                            subscriber.onError(e)
        //                        } catch (e: WriteException) {
        //                            subscriber.onError(e)
        //                        }
        //                    }
        //                }
    }

    private fun getTeamSessions(teamId: Int): Observable<List<Session>> {
        return sessionsRepository.getByTeamId(teamId).toList().toObservable()
    }


    private fun closeSession(time: Long, session: Session?): Observable<Session> {
        return if (session == null) Observable.empty() else sessionsRepository.closeSessions(time, session.id)
    }

    private fun setDriverForLatestPitSession(teamId: Int, driverId: Int): Observable<Session> {
        return sessionsRepository.getByTeamId(teamId)
                .filter { session -> session.type == Session.Type.PIT }.map { Optional.of(it) }.last(Optional.empty())
                .flatMapObservable { session -> if (session.isPresent) sessionsRepository.setSessionDriver(session.get()!!.id, driverId) else Observable.empty() }
    }
}