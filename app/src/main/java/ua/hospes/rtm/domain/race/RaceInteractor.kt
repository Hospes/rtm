package ua.hospes.rtm.domain.race

import io.reactivex.Observable
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.cars.CarsRepository
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.domain.preferences.PreferencesManager
import ua.hospes.rtm.domain.sessions.SessionsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RaceInteractor @Inject constructor(
        private val preferencesManager: PreferencesManager,
        private val raceRepository: RaceRepository,
        private val sessionsRepository: SessionsRepository,
        private val driversRepository: DriversRepository,
        private val carsRepository: CarsRepository) {

    val carsNotInRace: Observable<Car>
        get() = Observable.empty() //carsRepository.getNotInRace()


    //    fun setDriver(sessionId: Int, teamId: Int, driverId: Int): Observable<Boolean> {
    //        return Observable.empty()
    //        //        when (preferencesManager.pitStopAssign) {
    //        //            PitStopAssign.NEXT -> return Observable.zip(
    //        //                    setDriverForLatestPitSession(teamId, driverId),
    //        //                    sessionsRepository.setSessionDriver(sessionId, driverId),
    //        //                    { session, session2 -> true }
    //        //            )
    //        //            else -> return sessionsRepository.setSessionDriver(sessionId, driverId).map { session -> true }
    //        //        }
    //    }

    //
    //    fun teamOut(item: RaceItem, time: Long): Observable<Boolean> {
    //        return Observable.empty()
    //        //        var raceStartTime: Long = -1
    //        //        if (item.session != null) raceStartTime = item.session!!.raceStartTime
    //        //        return Observable.zip<Session, Session, RaceItem>(
    //        //                closeSession(time, item.session),
    //        //                sessionsRepository.startNewSessions(raceStartTime, time, Session.Type.TRACK, item.team.id),
    //        //                { closedSession, openedSession ->
    //        //                    item.session = openedSession
    //        //                    item
    //        //                })
    //        //                .toList()
    //        //                .flatMapObservable(Function<List<RaceItem>, ObservableSource<out Boolean>> { raceRepository.update(it) })
    //    }
    //
    //    fun removeLastSession(raceItem: RaceItem): Observable<Boolean> {
    //        return Observable.empty()
    //        //        return Observable.zip<Session, RaceItem, RaceItem>(sessionsRepository.removeLastSession(raceItem.team.id!!), Observable.just(raceItem), BiFunction<Session, RaceItem, RaceItem> { session, item -> this.updateRaceItemSession(session, item) })
    //        //                .toList()
    //        //                .flatMapObservable(Function<List<RaceItem>, ObservableSource<out Boolean>> { raceRepository.update(it) })
    //    }
    //
    //    fun removeAll(): Single<Void> {
    //        return Single.error(RuntimeException())
    //        //        return Single.zip(raceRepository.clear(), sessionsRepository.clear(), { aVoid, aVoid2 -> null })
    //    }
    //
    //
    //    fun exportXLS(): Single<File> {
    //        return Single.error(RuntimeException())
    //        //        return raceRepository.get()
    //        //                .map<Team>(Function<RaceItem, Team> { it.getTeam() })
    //        //                .flatMap { team -> Observable.zip<Team, List<Session>, Pair<Team, List<Session>>>(Observable.just(team), getTeamSessions(team.id!!), BiFunction<Team, List<Session>, Pair<Team, List<Session>>> { first, second -> Pair(first, second) }) }
    //        //                .toList().map<Map<Team, List<Session>>> { pairs ->
    //        //                    val data = HashMap<Team, List<Session>>()
    //        //                    for (pair in pairs) {
    //        //                        data[pair.first] = pair.second
    //        //                    }
    //        //                    data
    //        //                }
    //        //                .flatMap { teams ->
    //        //                    Single.create<File> { subscriber ->
    //        //                        try {
    //        //                            subscriber.onSuccess(XLSTest.createWorkbook(teams))
    //        //                        } catch (e: IOException) {
    //        //                            subscriber.onError(e)
    //        //                        } catch (e: WriteException) {
    //        //                            subscriber.onError(e)
    //        //                        }
    //        //                    }
    //        //                }
    //    }
    //
    //    private fun getTeamSessions(teamId: Int): Observable<List<Session>> {
    //        return sessionsRepository.getByTeam(teamId).toList().toObservable()
    //    }
    //
    //
    //    private fun closeSession(time: Long, session: Session?): Observable<Session> {
    //        return if (session == null) Observable.empty() else sessionsRepository.closeSessions(time, session.id)
    //    }
    //
    //    private fun setDriverForLatestPitSession(teamId: Int, driverId: Int): Observable<Session> {
    //        return sessionsRepository.getByTeam(teamId)
    //                .filter { session -> session.type == Session.Type.PIT }.map { Optional.of(it) }.last(Optional.empty())
    //                .flatMapObservable { session -> if (session.isPresent) sessionsRepository.setSessionDriver(session.get()!!.id, driverId) else Observable.empty() }
    //    }
}