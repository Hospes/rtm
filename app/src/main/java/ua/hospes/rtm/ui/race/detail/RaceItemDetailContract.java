package ua.hospes.rtm.ui.race.detail;

import java.util.List;

import ua.hospes.rtm.domain.race.models.RaceItem;
import ua.hospes.rtm.domain.sessions.models.Session;

/**
 * @author Andrew Khloponin
 */
public interface RaceItemDetailContract {
    interface View {
        void onUpdateRaceItem(RaceItem raceItem);

        void onUpdateSessions(List<Session> sessions);
    }
}