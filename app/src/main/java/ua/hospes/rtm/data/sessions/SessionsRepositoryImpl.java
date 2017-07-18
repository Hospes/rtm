package ua.hospes.rtm.data.sessions;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Emitter;
import rx.Observable;
import rx.Single;
import rx.functions.Action1;
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

/**
 * @author Andrew Khloponin
 */
@Singleton
public class SessionsRepositoryImpl implements SessionsRepository {
    private final SessionsDbStorage dbStorage;
    private final DriversRepository driversRepository;
    private final CarsRepository carsRepository;


    @Inject
    public SessionsRepositoryImpl(SessionsDbStorage dbStorage, DriversRepositoryImpl driversRepository, CarsRepositoryImpl carsRepository) {
        this.dbStorage = dbStorage;
        this.driversRepository = driversRepository;
        this.carsRepository = carsRepository;
    }


    @Override
    public Observable<Session> get() {
        return dbStorage.get()
                .flatMap(sessionItemDb ->
                        Observable.zip(
                                Observable.just(sessionItemDb),
                                getDriverById(sessionItemDb.getDriverId()),
                                getCarById(sessionItemDb.getCarId()),
                                SessionsMapper::map));
    }

    @Override
    public Observable<Session> get(int... id) {
        return dbStorage.get(id)
                .flatMap(sessionItemDb ->
                        Observable.zip(
                                Observable.just(sessionItemDb),
                                getDriverById(sessionItemDb.getDriverId()),
                                getCarById(sessionItemDb.getCarId()),
                                SessionsMapper::map));
    }

    @Override
    public Observable<Session> getByTeamId(int teamId) {
        return dbStorage.getByTeamId(teamId)
                .flatMap(sessionItemDb ->
                        Observable.zip(
                                Observable.just(sessionItemDb),
                                getDriverById(sessionItemDb.getDriverId()),
                                getCarById(sessionItemDb.getCarId()),
                                SessionsMapper::map));
    }

    @Override
    public Observable<Session> getByTeamIdAndDriverId(int teamId, int driverId) {
        return dbStorage.getByTeamIdAndDriverId(teamId, driverId)
                .flatMap(sessionItemDb ->
                        Observable.zip(
                                Observable.just(sessionItemDb),
                                getDriverById(sessionItemDb.getDriverId()),
                                getCarById(sessionItemDb.getCarId()),
                                SessionsMapper::map));
    }

    @Override
    public Observable<List<Session>> listen() {
        return dbStorage.listen()
                .flatMap(sessionItemDbs -> Observable.from(sessionItemDbs)
                        .flatMap(sessionItemDb ->
                                Observable.zip(
                                        Observable.just(sessionItemDb),
                                        getDriverById(sessionItemDb.getDriverId()),
                                        getCarById(sessionItemDb.getCarId()),
                                        SessionsMapper::map))
                        .toList());
    }

    @Override
    public Observable<List<Session>> listenByTeamId(int teamId) {
        return dbStorage.listenByTeamId(teamId)
                .flatMap(sessionItemDbs -> Observable.from(sessionItemDbs)
                        .flatMap(sessionItemDb ->
                                Observable.zip(
                                        Observable.just(sessionItemDb),
                                        getDriverById(sessionItemDb.getDriverId()),
                                        getCarById(sessionItemDb.getCarId()),
                                        SessionsMapper::map))
                        .toList());
    }

    @Override
    public Observable<Session> newSessions(Session.Type type, int... teamIds) {
        return Observable.create((Observable.OnSubscribe<SessionDb>) subscriber -> {
            for (int teamId : teamIds) {
                SessionDb sessionDb = new SessionDb(teamId);
                sessionDb.setType(type.name());
                subscriber.onNext(sessionDb);
            }
            subscriber.onCompleted();
        })
                .toList()
                .flatMap(dbStorage::add).map(result -> result.getData())
                .flatMap(sessionDb1 ->
                        Observable.zip(
                                Observable.just(sessionDb1),
                                getDriverById(sessionDb1.getDriverId()),
                                getCarById(sessionDb1.getCarId()),
                                SessionsMapper::map)
                );
    }

    @Override
    public Observable<Session> setSessionDriver(int sessionId, int driverId) {
        return Observable.just(new SetDriverOperation(sessionId, driverId))
                .toList()
                .flatMap(dbStorage::applySetDriverOperations)
                .flatMap(this::get);
    }

    @Override
    public Observable<Session> setSessionCar(int sessionId, int carId) {
        return Observable.just(new SetCarOperation(sessionId, carId))
                .toList()
                .flatMap(dbStorage::applySetCarOperations)
                .flatMap(this::get);
    }

    @Override
    public Observable<Session> startSessions(long raceStartTime, long startTime, int... sessionIds) {
        return Observable.create((Observable.OnSubscribe<OpenSessionOperation>) subscriber -> {
            for (int sessionId : sessionIds) {
                subscriber.onNext(new OpenSessionOperation(sessionId, raceStartTime, startTime));
            }
            subscriber.onCompleted();
        })
                .toList()
                .flatMap(dbStorage::applyOpenOperations)
                .flatMap(this::get);
    }

    @Override
    public Observable<Session> startNewSessions(long raceStartTime, long startTime, Session.Type type, int... teamIds) {
        return Observable.create((Observable.OnSubscribe<SessionDb>) subscriber -> {
            for (int teamId : teamIds) {
                SessionDb sessionDb = new SessionDb(teamId);
                sessionDb.setRaceStartTime(raceStartTime);
                sessionDb.setStartDurationTime(startTime);
                sessionDb.setType(type.name());
                subscriber.onNext(sessionDb);
            }
            subscriber.onCompleted();
        })
                .toList()
                .flatMap(dbStorage::add)
                .flatMap(result ->
                        Observable.zip(
                                Observable.just(result.getData()),
                                getDriverById(result.getData().getDriverId()),
                                getCarById(result.getData().getCarId()),
                                SessionsMapper::map)
                );
    }

    @Override
    public Observable<Session> startNewSession(long raceStartTime, long startTime, Session.Type type, int driverId, int teamId) {
        return Observable.create((Observable.OnSubscribe<SessionDb>) subscriber -> {
            SessionDb sessionDb = new SessionDb(teamId);
            sessionDb.setRaceStartTime(raceStartTime);
            sessionDb.setStartDurationTime(startTime);
            sessionDb.setDriverId(driverId);
            sessionDb.setType(type.name());
            subscriber.onNext(sessionDb);
            subscriber.onCompleted();
        })
                .toList()
                .flatMap(dbStorage::add)
                .flatMap(result ->
                        Observable.zip(
                                Observable.just(result.getData()),
                                getDriverById(result.getData().getDriverId()),
                                getCarById(result.getData().getCarId()),
                                SessionsMapper::map)
                );
    }

    @Override
    public Observable<Session> closeSessions(long stopTime, int... sessionIds) {
        return Observable.create((Action1<Emitter<CloseSessionOperation>>) emitter -> {
            for (int sessionId : sessionIds) {
                emitter.onNext(new CloseSessionOperation(sessionId, stopTime));
            }
            emitter.onCompleted();
        }, Emitter.BackpressureMode.NONE)
                .toList()
                .flatMap(dbStorage::applyCloseOperations)
                .flatMap(this::get);
    }

    @Override
    public Observable<Session> removeLastSession(int teamId) {
        return dbStorage.getByTeamId(teamId)
                .toSortedList((sessionDb, sessionDb2) -> Long.compare(sessionDb.getStartDurationTime(), sessionDb2.getStartDurationTime()))
                .flatMap(sessionDbs -> {
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
                .flatMap(session -> Observable.zip(
                        Observable.just(session),
                        getDriverById(session.getDriverId()),
                        getCarById(session.getCarId()),
                        SessionsMapper::map)
                );
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
                .toList().flatMap(dbStorage::applyOpenOperations).singleOrDefault(0).toSingle();
    }

    private Single<Integer> removeSession(SessionDb sessionDb) {
        return dbStorage.remove(sessionDb);
    }


    @Override
    public Single<Integer> removeAll() {
        return dbStorage.removeAll();
    }


    private Observable<Driver> getDriverById(int id) {
        return driversRepository.get(id).singleOrDefault(null, driver -> id == driver.getId());
    }

    private Observable<Car> getCarById(int id) {
        return carsRepository.getByIds(id).singleOrDefault(null, car -> id == car.getId());
    }
}