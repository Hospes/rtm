package ua.hospes.rtm.data.sessions;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Single;
import ua.hospes.dbhelper.InsertResult;
import ua.hospes.rtm.data.cars.CarsRepositoryImpl;
import ua.hospes.rtm.data.drivers.DriversRepositoryImpl;
import ua.hospes.rtm.data.sessions.mapper.SessionsMapper;
import ua.hospes.rtm.data.sessions.models.SessionDb;
import ua.hospes.rtm.data.sessions.operations.CloseSessionOperation;
import ua.hospes.rtm.data.sessions.operations.OpenSessionOperation;
import ua.hospes.rtm.data.sessions.operations.SetCarOperation;
import ua.hospes.rtm.data.sessions.operations.SetDriverOperation;
import ua.hospes.rtm.data.sessions.storage.SessionsDbStorage;
import ua.hospes.rtm.domain.cars.CarsRepository;
import ua.hospes.rtm.domain.cars.models.Car;
import ua.hospes.rtm.domain.drivers.DriversRepository;
import ua.hospes.rtm.domain.drivers.models.Driver;
import ua.hospes.rtm.domain.sessions.SessionsRepository;
import ua.hospes.rtm.domain.sessions.models.Session;
import ua.hospes.rtm.utils.Optional;

/**
 * @author Andrew Khloponin
 */
@Singleton
public class SessionsRepositoryImpl implements SessionsRepository {
    private final SessionsDbStorage dbStorage;
    private final DriversRepository driversRepository;
    private final CarsRepository    carsRepository;


    @Inject
    SessionsRepositoryImpl(SessionsDbStorage dbStorage, DriversRepositoryImpl driversRepository, CarsRepositoryImpl carsRepository) {
        this.dbStorage = dbStorage;
        this.driversRepository = driversRepository;
        this.carsRepository = carsRepository;
    }


    @Override
    public Observable<Session> get() {
        return dbStorage.get().flatMapSingle(this::transform);
    }

    @Override
    public Observable<Session> get(int... id) {
        return dbStorage.get(id).flatMapSingle(this::transform);
    }

    @Override
    public Observable<Session> getByTeamId(int teamId) {
        return dbStorage.getByTeamId(teamId).flatMapSingle(this::transform);
    }

    @Override
    public Observable<Session> getByTeamIdAndDriverId(int teamId, int driverId) {
        return dbStorage.getByTeamIdAndDriverId(teamId, driverId).flatMapSingle(this::transform);
    }

    @Override
    public Observable<List<Session>> listen() {
        return dbStorage.listen().flatMapSingle(this::transform);
    }

    @Override
    public Observable<List<Session>> listenByTeamId(int teamId) {
        return dbStorage.listenByTeamId(teamId).flatMapSingle(this::transform);
    }

    @Override
    public Observable<Session> newSessions(Session.Type type, int... teamIds) {
        return Observable.create((ObservableEmitter<SessionDb> subscriber) -> {
            for (int teamId : teamIds) {
                SessionDb sessionDb = new SessionDb(teamId);
                sessionDb.setType(type.name());
                subscriber.onNext(sessionDb);
            }
            subscriber.onComplete();
        })
                .toList()
                .flatMapObservable(dbStorage::add).map(result -> result.getData())
                .flatMapSingle(this::transform);
    }

    @Override
    public Observable<Session> setSessionDriver(int sessionId, int driverId) {
        return Observable.just(new SetDriverOperation(sessionId, driverId))
                .toList()
                .flatMapObservable(dbStorage::applySetDriverOperations)
                .flatMap(this::get);
    }

    @Override
    public Observable<Session> setSessionCar(int sessionId, int carId) {
        return Observable.just(new SetCarOperation(sessionId, carId))
                .toList()
                .flatMapObservable(dbStorage::applySetCarOperations)
                .flatMap(this::get);
    }

    @Override
    public Observable<Session> startSessions(long raceStartTime, long startTime, int... sessionIds) {
        return OpenSessionOperation.from(raceStartTime, startTime, sessionIds)
                .toList()
                .flatMapObservable(dbStorage::applyOpenOperations)
                .flatMap(this::get);
    }

    @Override
    public Observable<Session> startNewSessions(long raceStartTime, long startTime, Session.Type type, int... teamIds) {
        return Observable.create((ObservableEmitter<SessionDb> subscriber) -> {
            for (int teamId : teamIds) {
                SessionDb sessionDb = new SessionDb(teamId);
                sessionDb.setRaceStartTime(raceStartTime);
                sessionDb.setStartDurationTime(startTime);
                sessionDb.setType(type.name());
                subscriber.onNext(sessionDb);
            }
            subscriber.onComplete();
        })
                .toList()
                .flatMapObservable(dbStorage::add)
                .map(InsertResult::getData)
                .flatMapSingle(this::transform);
    }

    @Override
    public Observable<Session> startNewSession(long raceStartTime, long startTime, Session.Type type, int driverId, int teamId) {
        return Observable.create((ObservableEmitter<SessionDb> subscriber) -> {
            SessionDb sessionDb = new SessionDb(teamId);
            sessionDb.setRaceStartTime(raceStartTime);
            sessionDb.setStartDurationTime(startTime);
            sessionDb.setDriverId(driverId);
            sessionDb.setType(type.name());
            subscriber.onNext(sessionDb);
            subscriber.onComplete();
        })
                .toList()
                .flatMapObservable(dbStorage::add)
                .map(InsertResult::getData)
                .flatMapSingle(this::transform);
    }

    @Override
    public Observable<Session> closeSessions(long stopTime, int... sessionIds) {
        return CloseSessionOperation.from(stopTime, sessionIds)
                .toList()
                .flatMapObservable(dbStorage::applyCloseOperations)
                .flatMap(this::get);
    }

    @Override
    public Observable<Session> removeLastSession(int teamId) {
        return dbStorage.getByTeamId(teamId)
                .toSortedList((sessionDb, sessionDb2) -> Long.compare(sessionDb.getStartDurationTime(), sessionDb2.getStartDurationTime()))
                .flatMapObservable(sessionDbs -> {
                    int count = sessionDbs.size();
                    if (count >= 2) {
                        return Single.zip(
                                zipOpenSession(sessionDbs.get(count - 2)),
                                removeSession(sessionDbs.get(count - 1)),
                                (sdb1, i) -> sdb1
                        ).toObservable();
                    } else if (count == 1) {
                        return Observable.just(sessionDbs.get(0));
                    } else throw new RuntimeException("No sessions in the database");
                })
                .flatMapSingle(this::transform);
    }

    private Single<SessionDb> zipOpenSession(SessionDb sessionDb) {
        return Single.zip(
                Single.just(sessionDb),
                openSession(sessionDb.getId(), sessionDb.getRaceStartTime(), sessionDb.getStartDurationTime()),
                (sessionDb1, integer) -> sessionDb1
        );
    }

    private Single<Integer> openSession(int id, long raceStartTime, long startTime) {
        return Observable.just(new OpenSessionOperation(id, raceStartTime, startTime))
                .toList().flatMapObservable(dbStorage::applyOpenOperations).single(0);
    }

    private Single<Integer> removeSession(SessionDb sessionDb) {
        return dbStorage.remove(sessionDb);
    }


    @Override
    public Single<Integer> removeAll() {
        return dbStorage.removeAll();
    }


    private Single<Session> transform(SessionDb sessionDb) {
        return Single.zip(Single.just(sessionDb), getDriverById(sessionDb.getDriverId()), getCarById(sessionDb.getCarId()), SessionsMapper::map);
    }

    private Single<List<Session>> transform(List<SessionDb> sessionDbs) {
        return Observable.fromIterable(sessionDbs).flatMapSingle(this::transform).toList();
    }

    private Single<Optional<Driver>> getDriverById(int id) {
        return driversRepository.get(id).map(Optional::of).single(Optional.empty());
    }

    private Single<Optional<Car>> getCarById(int id) {
        return carsRepository.get(id).map(Optional::of).single(Optional.empty());
    }
}