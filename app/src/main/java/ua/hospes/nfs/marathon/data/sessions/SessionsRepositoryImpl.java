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
import ua.hospes.nfs.marathon.data.sessions.operations.SetDriverOperation;
import ua.hospes.nfs.marathon.data.sessions.storage.SessionsDbStorage;
import ua.hospes.nfs.marathon.domain.drivers.DriversRepository;
import ua.hospes.nfs.marathon.domain.drivers.models.Driver;
import ua.hospes.nfs.marathon.domain.sessions.SessionsRepository;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;
import ua.hospes.nfs.marathon.domain.team.TeamsRepository;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
@Singleton
public class SessionsRepositoryImpl implements SessionsRepository {
    private final SessionsDbStorage dbStorage;
    private final TeamsRepository teamsRepository;
    private final DriversRepository driversRepository;


    @Inject
    public SessionsRepositoryImpl(SessionsDbStorage dbStorage, TeamsRepository teamsRepository, DriversRepository driversRepository) {
        this.dbStorage = dbStorage;
        this.teamsRepository = teamsRepository;
        this.driversRepository = driversRepository;
    }


    @Override
    public Observable<Session> get() {
        return dbStorage.get()
                .flatMap(sessionItemDb ->
                        Observable.zip(
                                Observable.just(sessionItemDb),
                                getTeamById(sessionItemDb.getTeamId()),
                                getDriverById(sessionItemDb.getDriverId()),
                                SessionsMapper::map));
    }

    @Override
    public Observable<Session> get(int... id) {
        return dbStorage.get(id)
                .flatMap(sessionItemDb ->
                        Observable.zip(
                                Observable.just(sessionItemDb),
                                getTeamById(sessionItemDb.getTeamId()),
                                getDriverById(sessionItemDb.getDriverId()),
                                SessionsMapper::map));
    }

    @Override
    public Observable<List<Session>> listen() {
        return dbStorage.listen()
                .flatMap(sessionItemDbs -> Observable.from(sessionItemDbs)
                        .flatMap(sessionItemDb ->
                                Observable.zip(
                                        Observable.just(sessionItemDb),
                                        getTeamById(sessionItemDb.getTeamId()),
                                        getDriverById(sessionItemDb.getDriverId()),
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
                                        getTeamById(sessionItemDb.getTeamId()),
                                        getDriverById(sessionItemDb.getDriverId()),
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
                                getTeamById(result.getData().getTeamId()),
                                getDriverById(result.getData().getDriverId()),
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
                                getTeamById(result.getData().getTeamId()),
                                getDriverById(result.getData().getDriverId()),
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


    private Observable<Team> getTeamById(int id) {
        return teamsRepository.get(id).singleOrDefault(null, team -> id == team.getId());
    }

    private Observable<Driver> getDriverById(int id) {
        return driversRepository.get(id).singleOrDefault(null, driver -> id == driver.getId());
    }
}