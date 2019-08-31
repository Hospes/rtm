package ua.hospes.rtm.data.sessions

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import ua.hospes.rtm.data.sessions.mapper.SessionsMapper
import ua.hospes.rtm.data.sessions.models.SessionDb
import ua.hospes.rtm.data.sessions.operations.CloseSessionOperation
import ua.hospes.rtm.data.sessions.operations.OpenSessionOperation
import ua.hospes.rtm.data.sessions.operations.SetCarOperation
import ua.hospes.rtm.data.sessions.operations.SetDriverOperation
import ua.hospes.rtm.data.sessions.storage.SessionsDbStorage
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.domain.cars.CarsRepository
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.domain.drivers.DriversRepository
import ua.hospes.rtm.domain.sessions.Session
import ua.hospes.rtm.domain.sessions.SessionsRepository
import ua.hospes.rtm.utils.Optional
import javax.inject.Inject

class SessionsRepositoryImpl @Inject constructor(
        private val dbStorage: SessionsDbStorage,
        private val driversRepository: DriversRepository,
        private val carsRepository: CarsRepository
) : SessionsRepository {


    override fun get(): Observable<Session> = dbStorage.get().flatMapSingle { transform(it) }

    override fun get(vararg ids: Int): Observable<Session> = dbStorage.get(*ids).flatMapSingle { transform(it) }

    override fun getByTeamId(teamId: Int): Observable<Session> = dbStorage.getByTeamId(teamId).flatMapSingle { transform(it) }

    override fun getByTeamIdAndDriverId(teamId: Int, driverId: Int): Observable<Session> =
            dbStorage.getByTeamIdAndDriverId(teamId, driverId).flatMapSingle { transform(it) }

    override fun listen(): Observable<List<Session>> = dbStorage.listen().flatMapSingle { transform(it) }

    override fun listenByTeamId(teamId: Int): Observable<List<Session>> = dbStorage.listenByTeamId(teamId).flatMapSingle { transform(it) }

    override fun newSessions(type: Session.Type, vararg teamIds: Int): Observable<Session> =
            Observable.create { subscriber: ObservableEmitter<SessionDb> ->
                for (teamId in teamIds) {
                    val sessionDb = SessionDb(teamId)
                    sessionDb.type = type.name
                    subscriber.onNext(sessionDb)
                }
                subscriber.onComplete()
            }
                    .toList()
                    .flatMapObservable { dbStorage.add(it) }.map { it.data }
                    .flatMapSingle { transform(it) }

    override fun setSessionDriver(sessionId: Int, driverId: Int): Observable<Session> =
            Observable.just(SetDriverOperation(sessionId, driverId))
                    .toList()
                    .flatMapObservable { dbStorage.applySetDriverOperations(it) }
                    .flatMap { get(it) }

    override fun setSessionCar(sessionId: Int, carId: Int): Observable<Session> =
            Observable.just(SetCarOperation(sessionId, carId))
                    .toList()
                    .flatMapObservable { dbStorage.applySetCarOperations(it) }
                    .flatMap { get(it) }

    override fun startSessions(raceStartTime: Long, startTime: Long, vararg sessionIds: Int): Observable<Session> =
            OpenSessionOperation.from(raceStartTime, startTime, *sessionIds)
                    .toList()
                    .flatMapObservable { dbStorage.applyOpenOperations(it) }
                    .flatMap { get(it) }

    override fun startNewSessions(raceStartTime: Long, startTime: Long, type: Session.Type, vararg teamIds: Int): Observable<Session> =
            Observable.create { subscriber: ObservableEmitter<SessionDb> ->
                for (teamId in teamIds) {
                    val sessionDb = SessionDb(teamId)
                    sessionDb.raceStartTime = raceStartTime
                    sessionDb.startDurationTime = startTime
                    sessionDb.type = type.name
                    subscriber.onNext(sessionDb)
                }
                subscriber.onComplete()
            }
                    .toList()
                    .flatMapObservable { dbStorage.add(it) }.map { it.data }
                    .flatMapSingle { transform(it) }

    override fun startNewSession(raceStartTime: Long, startTime: Long, type: Session.Type, driverId: Int, teamId: Int): Observable<Session> =
            Observable.create { subscriber: ObservableEmitter<SessionDb> ->
                val sessionDb = SessionDb(teamId)
                sessionDb.raceStartTime = raceStartTime
                sessionDb.startDurationTime = startTime
                sessionDb.driverId = driverId
                sessionDb.type = type.name
                subscriber.onNext(sessionDb)
                subscriber.onComplete()
            }
                    .toList()
                    .flatMapObservable { dbStorage.add(it) }.map { it.data }
                    .flatMapSingle { transform(it) }

    override fun closeSessions(stopTime: Long, vararg sessionIds: Int): Observable<Session> =
            CloseSessionOperation.from(stopTime, *sessionIds)
                    .toList()
                    .flatMapObservable { dbStorage.applyCloseOperations(it) }
                    .flatMap { get(it) }

    override fun removeLastSession(teamId: Int): Observable<Session> =
            dbStorage.getByTeamId(teamId)
                    .toSortedList { o1, o2 -> o1.startDurationTime.compareTo(o2.startDurationTime) }
                    .flatMapObservable { list ->
                        val count = list.size
                        when {
                            count >= 2 -> dbStorage.getByTeamId(teamId)
                                    .toSortedList { o1, o2 -> o1.startDurationTime.compareTo(o2.startDurationTime) }
                                    .flatMapObservable {
                                        Single.zip(
                                                zipOpenSession(it[count - 2]),
                                                removeSession(it[count - 1]),
                                                BiFunction { t1: SessionDb, _: Int -> t1 }
                                        ).toObservable()
                                    }

                            count == 1 ->
                                dbStorage.getByTeamId(teamId)
                                        .toSortedList { o1, o2 -> o1.startDurationTime.compareTo(o2.startDurationTime) }
                                        .flatMapObservable { Observable.just(it.first()) }
                            else -> throw RuntimeException("No sessions in the database")
                        }
                    }
                    .flatMapSingle { transform(it) }

    private fun zipOpenSession(sessionDb: SessionDb): Single<SessionDb> = Single.zip(
            Single.just(sessionDb),
            openSession(sessionDb.id, sessionDb.raceStartTime, sessionDb.startDurationTime),
            BiFunction { sessionDb1, _ -> sessionDb1 }
    )

    private fun openSession(id: Int, raceStartTime: Long, startTime: Long): Single<Int> =
            Observable.just(OpenSessionOperation(id, raceStartTime, startTime))
                    .toList()
                    .flatMapObservable { dbStorage.applyOpenOperations(it) }
                    .single(0)

    private fun removeSession(sessionDb: SessionDb): Single<Int> = dbStorage.remove(sessionDb)


    override fun removeAll(): Single<Int> = dbStorage.removeAll()


    private fun transform(sessionDb: SessionDb): Single<Session> = Single.zip(
            Single.just(sessionDb), getDriverById(sessionDb.driverId), getCarById(sessionDb.carId),
            Function3 { db, driver, car -> SessionsMapper.map(db, driver, car) }
    )

    private fun transform(sessionDbs: List<SessionDb>): Single<List<Session>> =
            Observable.fromIterable(sessionDbs).flatMapSingle { transform(it) }.toList()

    private fun getDriverById(id: Int): Single<Optional<Driver>> =
            driversRepository[id].map { drivers -> Optional.of(drivers.first()) }

    private fun getCarById(id: Int): Single<Optional<Car>> =
            carsRepository.get(id).map { Optional.of(it) }.single(Optional.empty())
}