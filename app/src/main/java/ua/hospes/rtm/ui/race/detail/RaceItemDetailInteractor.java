package ua.hospes.rtm.ui.race.detail;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import ua.hospes.rtm.domain.race.RaceRepository;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.sessions.SessionsRepository;
import ua.hospes.rtm.domain.sessions.models.Session;

/**
 * @author Andrew Khloponin
 */
public class RaceItemDetailInteractor {
    private final RaceRepository raceRepository;
    private final SessionsRepository sessionsRepository;


    @Inject
    public RaceItemDetailInteractor(RaceRepository raceRepository, SessionsRepository sessionsRepository) {
        this.raceRepository = raceRepository;
        this.sessionsRepository = sessionsRepository;
    }


    public Observable<RaceItem> listenRaceItem(int raceItemId) {
        return raceRepository.listen()
                .flatMap(items -> Observable.from(items).singleOrDefault(null, item -> item.getId() == raceItemId));
    }

    public Observable<List<Session>> listenSessions(int teamId) {
        return sessionsRepository.listenByTeamId(teamId);
    }
}