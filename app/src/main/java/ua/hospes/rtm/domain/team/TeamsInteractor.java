package ua.hospes.rtm.domain.team;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.rtm.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
public class TeamsInteractor {
    private final TeamsRepository repository;


    @Inject
    public TeamsInteractor(TeamsRepository repository) {
        this.repository = repository;
    }


    public Observable<List<Team>> listen() {
        return repository.listen();
    }

    public Observable<Void> clear() {
        return repository.clear();
    }
}