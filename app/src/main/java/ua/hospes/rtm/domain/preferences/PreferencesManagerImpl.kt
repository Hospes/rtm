package ua.hospes.rtm.domain.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import javax.inject.Inject
import javax.inject.Singleton

import ua.hospes.rtm.domain.race.models.PitStopAssign

@Singleton
class PreferencesManagerImpl @Inject
internal constructor(context: Context) : AbsPreferencesManager(), PreferencesManager {
    override val preferences: SharedPreferences


    //Additional functionality
    override val pitStopAssign: PitStopAssign
        get() {
            try {
                return PitStopAssign.fromString(getString(KEY_ASSIGN_PITSTOP, "manual"))
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                return PitStopAssign.MANUAL
            }

        }

    override val sessionButtonType: String?
        get() = getString(KEY_SESSION_BUTTON_TYPE, "pit")

    override val isExportXLSEnabled: Boolean
        get() = getBoolean(KEY_ENABLE_EXPORT_XLS, false)

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    companion object {


        private val KEY_ASSIGN_PITSTOP = "assign_pitstop_dur_to"
        private val KEY_SESSION_BUTTON_TYPE = "session_button_type"
        private val KEY_ENABLE_EXPORT_XLS = "enable_export_xls"
    }
}