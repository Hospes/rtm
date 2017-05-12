package ua.hospes.rtm.ui.race;

import java.util.List;

import ua.hospes.rtm.domain.race.models.RaceItem;

/**
 * @author Andrew Khloponin
 */
interface RaceContract {
    interface View {
        void update(List<RaceItem> items);
    }
}