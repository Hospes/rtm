package ua.hospes.nfs.marathon.domain.preferences;

import ua.hospes.nfs.marathon.domain.race.models.PitStopAssign;

/**
 * @author Andrew Khloponin
 */
public interface PreferencesManager {
    PitStopAssign getPitStopAssign();

    boolean isPitStopSessionsRemoved();
}