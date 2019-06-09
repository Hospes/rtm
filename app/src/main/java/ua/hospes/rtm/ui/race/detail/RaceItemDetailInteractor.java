package ua.hospes.rtm.ui.race.detail;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ua.hospes.rtm.domain.race.RaceRepository;
import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.sessions.Session;
import ua.hospes.rtm.domain.sessions.SessionsRepository;

/**
 * @author Andrew Khloponin
 */
class RaceItemDetailInteractor {
    private final RaceRepository raceRepository;
    private final SessionsRepository sessionsRepository;


    @Inject
    RaceItemDetailInteractor(RaceRepository raceRepository, SessionsRepository sessionsRepository) {
        this.raceRepository = raceRepository;
        this.sessionsRepository = sessionsRepository;
    }


    Observable<RaceItem> listenRaceItem(int raceItemId) {
        return raceRepository.listen(raceItemId);
    }

    Observable<List<Session>> listenSessions(int teamId) {
        return sessionsRepository.listenByTeamId(teamId);
    }
}