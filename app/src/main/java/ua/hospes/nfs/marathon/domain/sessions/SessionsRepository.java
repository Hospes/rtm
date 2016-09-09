package ua.hospes.nfs.marathon.domain.sessions;

import java.util.List;

import rx.Observable;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;

/**
 * @author Andrew Khloponin
 */
public interface SessionsRepository {
    Observable<Session> get();

    Observable<List<Session>> listen();
}