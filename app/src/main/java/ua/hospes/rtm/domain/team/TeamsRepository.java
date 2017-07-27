package ua.hospes.rtm.domain.team;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
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

    Observable<Boolean> save(Team team);

    Single<Integer> remove(int id);

    Single<Integer> removeAll();
}