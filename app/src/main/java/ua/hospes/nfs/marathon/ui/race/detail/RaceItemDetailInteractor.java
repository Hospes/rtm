package ua.hospes.nfs.marathon.ui.race.detail;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.nfs.marathon.core.di.scope.ActivityScope;
import ua.hospes.nfs.marathon.domain.race.RaceRepository;
import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.domain.sessions.SessionsRepository;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;

/**
 * @author Andrew Khloponin
 */
@ActivityScope
public class RaceItemDetailInteractor {
    @Inject RaceRepository raceRepository;
    @Inject SessionsRepository sessionsRepository;

    @Inject
    public RaceItemDetailInteractor() {}

    public Observable<RaceItem> listenRaceItem(int raceItemId) {
        return raceRepository.listen()
                .flatMap(items -> Observable.from(items).singleOrDefault(null, item -> item.getId() == raceItemId));
    }

    public Observable<List<Session>> listenSessions(int teamId) {
        return sessionsRepository.listenByTeamId(teamId);
    }
}