package ua.hospes.nfs.marathon.data.sessions;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import ua.hospes.nfs.marathon.data.sessions.mapper.SessionsMapper;
import ua.hospes.nfs.marathon.data.sessions.storage.SessionsDbStorage;
import ua.hospes.nfs.marathon.domain.sessions.SessionsRepository;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;

/**
 * @author Andrew Khloponin
 */
@Singleton
public class SessionsRepositoryImpl implements SessionsRepository {
    private final SessionsDbStorage dbStorage;


    @Inject
    public SessionsRepositoryImpl(SessionsDbStorage dbStorage) {
        this.dbStorage = dbStorage;
    }


    @Override
    public Observable<Session> get() {
        return dbStorage.get().map(SessionsMapper::map);
    }

    @Override
    public Observable<List<Session>> listen() {
        return dbStorage.listen().map(SessionsMapper::map);
    }
}