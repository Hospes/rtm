package ua.hospes.nfs.marathon.data.sessions;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import ua.hospes.nfs.marathon.data.sessions.mapper.SessionsMapper;
import ua.hospes.nfs.marathon.data.sessions.models.SessionDb;
import ua.hospes.nfs.marathon.data.sessions.operations.CloseSessionOperation;
import ua.hospes.nfs.marathon.data.sessions.operations.OpenSessionOperation;
import ua.hospes.nfs.marathon.data.sessions.operations.SetCarOperation;
import ua.hospes.nfs.marathon.data.sessions.operations.SetDriverOperation;
import ua.hospes.nfs.marathon.data.sessions.storage.SessionsDbStorage;
import ua.hospes.nfs.marathon.domain.cars.CarsRepository;
import ua.hospes.nfs.marathon.domain.cars.models.Car;
import ua.hospes.nfs.marathon.domain.drivers.DriversRepository;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.domain.sessions.SessionsRepository;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;

/**
 * @author Andrew Khloponin
 */
@Singleton
public class SessionsRepositoryImpl implements SessionsRepository {
    private final SessionsDbStorage dbStorage;
    private final DriversRepository driversRepository;
    private final CarsRepository carsRepository;


    @Inject
    public SessionsRepositoryImpl(SessionsDbStorage dbStorage, DriversRepository driversRepository, CarsRepository carsRepository) {
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
        return Observable.create(new Observable.OnSubscribe<SessionDb>() {
            @Override
            public void call(Subscriber<? super SessionDb> subscriber) {
                for (int teamId : teamIds) {
                    SessionDb sessionDb = new SessionDb(teamId);
                    sessionDb.setType(type.name());
                    subscriber.onNext(sessionDb);
                }
                subscriber.onCompleted();
            }
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
    public Observable<Session> startSessions(long startTime, int... sessionIds) {
        return Observable.create(new Observable.OnSubscribe<OpenSessionOperation>() {
            @Override
            public void call(Subscriber<? super OpenSessionOperation> subscriber) {
                for (int sessionId : sessionIds) {
                    subscriber.onNext(new OpenSessionOperation(sessionId, startTime));
                }
                subscriber.onCompleted();
            }
        })
                .toList()
                .flatMap(dbStorage::applyOpenOperations)
                .flatMap(this::get);
    }

    @Override
    public Observable<Session> startNewSessions(long startTime, Session.Type type, int... teamIds) {
        return Observable.create(new Observable.OnSubscribe<SessionDb>() {
            @Override
            public void call(Subscriber<? super SessionDb> subscriber) {
                for (int teamId : teamIds) {
                    SessionDb sessionDb = new SessionDb(teamId);
                    sessionDb.setStartDurationTime(startTime);
                    sessionDb.setType(type.name());
                    subscriber.onNext(sessionDb);
                }
                subscriber.onCompleted();
            }
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
    public Observable<Session> startNewSession(long startTime, Session.Type type, int driverId, int teamId) {
        return Observable.create(new Observable.OnSubscribe<SessionDb>() {
            @Override
            public void call(Subscriber<? super SessionDb> subscriber) {
                SessionDb sessionDb = new SessionDb(teamId);
                sessionDb.setStartDurationTime(startTime);
                sessionDb.setDriverId(driverId);
                sessionDb.setType(type.name());
                subscriber.onNext(sessionDb);
                subscriber.onCompleted();
            }
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
        return Observable.create(new Observable.OnSubscribe<CloseSessionOperation>() {
            @Override
            public void call(Subscriber<? super CloseSessionOperation> subscriber) {
                for (int sessionId : sessionIds) {
                    subscriber.onNext(new CloseSessionOperation(sessionId, stopTime));
                }
                subscriber.onCompleted();
            }
        })
                .toList()
                .flatMap(dbStorage::applyCloseOperations)
                .flatMap(this::get);
    }


    @Override
    public Observable<Void> clean() {
        return dbStorage.clean();
    }


    private Observable<Driver> getDriverById(int id) {
        return driversRepository.get(id).singleOrDefault(null, driver -> id == driver.getId());
    }

    private Observable<Car> getCarById(int id) {
        return carsRepository.getByIds(id).singleOrDefault(null, car -> id == car.getId());
    }
}