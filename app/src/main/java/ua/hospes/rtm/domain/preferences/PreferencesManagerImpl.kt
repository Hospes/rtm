package ua.hospes.rtm.domain.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import ua.hospes.rtm.domain.race.models.PitStopAssign

private const val KEY_ASSIGN_PITSTOP = "assign_pitstop_dur_to"
private const val KEY_SESSION_BUTTON_TYPE = "session_button_type"
private const val KEY_ENABLE_EXPORT_XLS = "enable_export_xls"
private const val KEY_PRIVACY = "privacy_accepted"
private const val KEY_TERMS = "terms_accepted"

class PreferencesManagerImpl constructor(context: Context) : AbsPreferencesManager(), PreferencesManager {

    override val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)


    //Additional functionality
    override val pitStopAssign: PitStopAssign
        get() = try {
            PitStopAssign.fromString(getString(KEY_ASSIGN_PITSTOP, "manual") ?: "manual")
        } catch (e: IllegalArgumentException) {
            PitStopAssign.MANUAL
        }

    override val sessionButtonType: String
        get() = getString(KEY_SESSION_BUTTON_TYPE, "pit") ?: "pit"

    override val isExportXLSEnabled: Boolean
        get() = getBoolean(KEY_ENABLE_EXPORT_XLS, false)


    override var isPrivacyAccepted: Boolean
        get() = getBoolean(KEY_PRIVACY, false)
        set(value) = putBoolean(KEY_PRIVACY, value)

    override var isTermsAccepted: Boolean
        get() = getBoolean(KEY_TERMS, false)
        set(value) = putBoolean(KEY_TERMS, value)
}