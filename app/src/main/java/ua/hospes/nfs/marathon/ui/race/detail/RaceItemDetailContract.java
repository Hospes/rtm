package ua.hospes.nfs.marathon.ui.race.detail;

import java.util.List;

import ua.hospes.nfs.marathon.domain.race.models.RaceItem;
import ua.hospes.nfs.marathon.domain.sessions.models.Session;

/**
 * @author Andrew Khloponin
 */
public interface RaceItemDetailContract {
    interface View {
        void onUpdateDetails(RaceItem raceItem);

        void onUpdateSessions(List<Session> sessions);
    }
}