package ua.hospes.rtm.data.sessions

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import ua.hospes.dbhelper.InsertResult
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

/**
 * @author Andrew Khloponin
 */
@Singleton
class SessionsRepositoryImpl @Inject
internal constructor(private val dbStorage: SessionsDbStorage, private val driversRepository: DriversRepository, private val carsRepository: CarsRepository) : SessionsRepository {


    override fun get(): Observable<Session> {
        return dbStorage.get().flatMapSingle(Function<SessionDb, SingleSource<out Session>> { this.transform(it) })
    }

    override fun get(vararg id: Int): Observable<Session> {
        return dbStorage.get(*id).flatMapSingle(Function<SessionDb, SingleSource<out Session>> { this.transform(it) })
    }

    override fun getByTeamId(teamId: Int): Observable<Session> {
        return dbStorage.getByTeamId(teamId).flatMapSingle(Function<SessionDb, SingleSource<out Session>> { this.transform(it) })
    }

    override fun getByTeamIdAndDriverId(teamId: Int, driverId: Int): Observable<Session> {
        return dbStorage.getByTeamIdAndDriverId(teamId, driverId).flatMapSingle(Function<SessionDb, SingleSource<out Session>> { this.transform(it) })
    }

    override fun listen(): Observable<List<Session>> {
        return dbStorage.listen().flatMapSingle(Function<List<SessionDb>, SingleSource<out List<Session>>> { this.transform(it) })
    }

    override fun listenByTeamId(teamId: Int): Observable<List<Session>> {
        return dbStorage.listenByTeamId(teamId).flatMapSingle(Function<List<SessionDb>, SingleSource<out List<Session>>> { this.transform(it) })
    }

    override fun newSessions(type: Session.Type, vararg teamIds: Int): Observable<Session> {
        return Observable.create { subscriber: ObservableEmitter<SessionDb> ->
            for (teamId in teamIds) {
                val sessionDb = SessionDb(teamId)
                sessionDb.type = type.name
                subscriber.onNext(sessionDb)
            }
            subscriber.onComplete()
        }
                .toList()
                .flatMapObservable<InsertResult<SessionDb>>(Function<List<SessionDb>, ObservableSource<out InsertResult<SessionDb>>> { dbStorage.add(it) }).map { result -> result.data }
                .flatMapSingle(Function<SessionDb, SingleSource<out Session>> { this.transform(it) })
    }

    override fun setSessionDriver(sessionId: Int, driverId: Int): Observable<Session> {
        return Observable.just(SetDriverOperation(sessionId, driverId))
                .toList()
                .flatMapObservable<Int>(Function<List<SetDriverOperation>, ObservableSource<out Int>> { dbStorage.applySetDriverOperations(it) })
                .flatMap(Function<Int, ObservableSource<out Session>> { this.get(it) })
    }

    override fun setSessionCar(sessionId: Int, carId: Int): Observable<Session> {
        return Observable.just(SetCarOperation(sessionId, carId))
                .toList()
                .flatMapObservable<Int>(Function<List<SetCarOperation>, ObservableSource<out Int>> { dbStorage.applySetCarOperations(it) })
                .flatMap(Function<Int, ObservableSource<out Session>> { this.get(it) })
    }

    override fun startSessions(raceStartTime: Long, startTime: Long, vararg sessionIds: Int): Observable<Session> {
        return OpenSessionOperation.from(raceStartTime, startTime, *sessionIds)
                .toList()
                .flatMapObservable<Int>(Function<List<OpenSessionOperation>, ObservableSource<out Int>> { dbStorage.applyOpenOperations(it) })
                .flatMap(Function<Int, ObservableSource<out Session>> { this.get(it) })
    }

    override fun startNewSessions(raceStartTime: Long, startTime: Long, type: Session.Type, vararg teamIds: Int): Observable<Session> {
        return Observable.create { subscriber: ObservableEmitter<SessionDb> ->
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
                .flatMapObservable<InsertResult<SessionDb>>(Function<List<SessionDb>, ObservableSource<out InsertResult<SessionDb>>> { dbStorage.add(it) })
                .map<SessionDb>(Function<InsertResult<SessionDb>, SessionDb> { it.getData() })
                .flatMapSingle(Function<SessionDb, SingleSource<out Session>> { this.transform(it) })
    }

    override fun startNewSession(raceStartTime: Long, startTime: Long, type: Session.Type, driverId: Int, teamId: Int): Observable<Session> {
        return Observable.create { subscriber: ObservableEmitter<SessionDb> ->
            val sessionDb = SessionDb(teamId)
            sessionDb.raceStartTime = raceStartTime
            sessionDb.startDurationTime = startTime
            sessionDb.driverId = driverId
            sessionDb.type = type.name
            subscriber.onNext(sessionDb)
            subscriber.onComplete()
        }
                .toList()
                .flatMapObservable<InsertResult<SessionDb>>(Function<List<SessionDb>, ObservableSource<out InsertResult<SessionDb>>> { dbStorage.add(it) })
                .map<SessionDb>(Function<InsertResult<SessionDb>, SessionDb> { it.getData() })
                .flatMapSingle(Function<SessionDb, SingleSource<out Session>> { this.transform(it) })
    }

    override fun closeSessions(stopTime: Long, vararg sessionIds: Int): Observable<Session> {
        return CloseSessionOperation.from(stopTime, *sessionIds)
                .toList()
                .flatMapObservable<Int>(Function<List<CloseSessionOperation>, ObservableSource<out Int>> { dbStorage.applyCloseOperations(it) })
                .flatMap(Function<Int, ObservableSource<out Session>> { this.get(it) })
    }

    override fun removeLastSession(teamId: Int): Observable<Session> {
        return dbStorage.getByTeamId(teamId)
                .toSortedList { sessionDb, sessionDb2 -> java.lang.Long.compare(sessionDb.startDurationTime, sessionDb2.startDurationTime) }
                .flatMapObservable<SessionDb> { sessionDbs ->
                    val count = sessionDbs.size
                    if (count >= 2) {
                        return@dbStorage.getByTeamId(teamId)
                                .toSortedList(sessionDb, sessionDb2) -> Long.compare(sessionDb.getStartDurationTime(), sessionDb2.getStartDurationTime()))
                        .flatMapObservable Single . zip < SessionDb, Int, SessionDb>(
                        zipOpenSession(sessionDbs[count - 2]),
                        removeSession(sessionDbs[count - 1]),
                        { sdb1, i -> sdb1 }
                        ).toObservable()
                    } else if (count == 1) {
                        return@dbStorage.getByTeamId(teamId)
                                .toSortedList(sessionDb, sessionDb2) -> Long.compare(sessionDb.getStartDurationTime(), sessionDb2.getStartDurationTime()))
                        .flatMapObservable Observable . just < SessionDb >(sessionDbs[0])
                    } else
                        throw RuntimeException("No sessions in the database")
                }
                .flatMapSingle(Function<SessionDb, SingleSource<out Session>> { this.transform(it) })
    }

    private fun zipOpenSession(sessionDb: SessionDb): Single<SessionDb> {
        return Single.zip(
                Single.just(sessionDb),
                openSession(sessionDb.id, sessionDb.raceStartTime, sessionDb.startDurationTime),
                { sessionDb1, integer -> sessionDb1 }
        )
    }

    private fun openSession(id: Int, raceStartTime: Long, startTime: Long): Single<Int> {
        return Observable.just(OpenSessionOperation(id, raceStartTime, startTime))
                .toList().flatMapObservable<Int>(Function<List<OpenSessionOperation>, ObservableSource<out Int>> { dbStorage.applyOpenOperations(it) }).single(0)
    }

    private fun removeSession(sessionDb: SessionDb): Single<Int> {
        return dbStorage.remove(sessionDb)
    }


    override fun removeAll(): Single<Int> {
        return dbStorage.removeAll()
    }


    private fun transform(sessionDb: SessionDb): Single<Session> {
        return Single.zip(Single.just(sessionDb), getDriverById(sessionDb.driverId), getCarById(sessionDb.carId), Function3<SessionDb, Optional<Driver>, Optional<Car>, Session> { db, driver, car -> SessionsMapper.map(db, driver, car) })
    }

    private fun transform(sessionDbs: List<SessionDb>): Single<List<Session>> {
        return Observable.fromIterable(sessionDbs).flatMapSingle<Session>(Function<SessionDb, SingleSource<out Session>> { this.transform(it) }).toList()
    }

    private fun getDriverById(id: Int): Single<Optional<Driver>> {
        return driversRepository[id].map { drivers -> drivers[0] }.map(Function<Driver, Optional<Driver>> { Optional.of(it) })
    }

    private fun getCarById(id: Int): Single<Optional<Car>> {
        return carsRepository.get(id).map<Optional<Car>>(Function<Car, Optional<Car>> { Optional.of(it) }).single(Optional.empty())
    }
}