package ua.hospes.rtm.domain.preferences

import ua.hospes.rtm.domain.race.models.PitStopAssign

/**
 * @author Andrew Khloponin
 */
interface PreferencesManager {
    val pitStopAssign: PitStopAssign

    val sessionButtonType: String

    val isExportXLSEnabled: Boolean
}