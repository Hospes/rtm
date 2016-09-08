package ua.hospes.nfs.marathon.domain.team;

import java.util.List;

import rx.Observable;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public interface TeamsRepository {
    Observable<Team> get();

    Observable<List<Team>> listen();

    Observable<Boolean> save(Team driver);

    Observable<Boolean> delete(Team driver);
}