package ua.hospes.nfs.marathon.domain.team;

import java.util.List;

import rx.Observable;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public interface TeamsRepository {
    /**
     * Get all teams from database
     */
    Observable<Team> get();

    Observable<Team> get(int id);

    /**
     * Get teams that not yet in Race
     */
    Observable<Team> getNotInRace();

    Observable<List<Team>> listen();

    Observable<Boolean> save(Team driver);

    Observable<Boolean> delete(Team driver);
}