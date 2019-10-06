package ua.hospes.rtm.domain.preferences

import ua.hospes.rtm.domain.race.models.PitStopAssign

interface PreferencesManager {
    val pitStopAssign: PitStopAssign

    val sessionButtonType: String

    val isExportXLSEnabled: Boolean


    var isPrivacyAccepted: Boolean
    var isTermsAccepted: Boolean
}