package ua.hospes.nfs.marathon.domain.team;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.nfs.marathon.core.di.scope.ActivityScope;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
@ActivityScope
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