package ua.hospes.nfs.marathon.ui.race;

import java.util.List;

import ua.hospes.nfs.marathon.domain.race.models.RaceItem;

/**
 * @author Andrew Khloponin
 */
public interface RaceContract {
    interface View {
        void update(List<RaceItem> items);
    }
}