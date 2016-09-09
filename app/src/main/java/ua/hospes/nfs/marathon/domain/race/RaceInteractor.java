package ua.hospes.nfs.marathon.domain.race;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.nfs.marathon.core.di.scope.ActivityScope;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.domain.team.TeamsRepository;
import ua.hospes.nfs.marathon.domain.team.models.Team;

/**
 * @author Andrew Khloponin
 */
@ActivityScope
public class RaceInteractor {
    private final RaceRepository repository;
    private final TeamsRepository teamsRepository;


    @Inject
    public RaceInteractor(RaceRepository repository, TeamsRepository teamsRepository) {
        this.repository = repository;
        this.teamsRepository = teamsRepository;
    }


    public Observable<List<RaceItem>> listen() {
        return repository.listen();
    }

    public Observable<List<Team>> getNotInRace() {
        return teamsRepository.getNotInRace().toList();
    }
}