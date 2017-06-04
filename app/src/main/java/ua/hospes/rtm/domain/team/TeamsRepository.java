package ua.hospes.rtm.domain.team;

import java.util.List;

import rx.Observable;
import rx.Single;
import ua.hospes.rtm.domain.team.models.Team;

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

    Single<Integer> remove(Team driver);

    Single<Integer> removeAll();
}