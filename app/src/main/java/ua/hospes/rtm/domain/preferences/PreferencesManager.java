package ua.hospes.rtm.domain.preferences;

import ua.hospes.rtm.domain.race.models.PitStopAssign;

/**
 * @author Andrew Khloponin
 */
public interface PreferencesManager {
    PitStopAssign getPitStopAssign();

    boolean isPitStopSessionsRemoved();

    boolean isExportXLSEnabled();
}