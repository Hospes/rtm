package ua.hospes.rtm.ui.race;

import android.content.Context;

import java.util.List;

import ua.hospes.rtm.domain.race.models.RaceItem;

/**
 * @author Andrew Khloponin
 */
interface RaceContract {
    interface View {
        Context getContext();

        void update(List<RaceItem> items);
    }
}