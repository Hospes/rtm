package ua.hospes.rtm.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import dagger.android.support.AndroidSupportInjection
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.preferences.PreferencesManager
import java.util.*
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {
    @Inject lateinit var preferencesManager: PreferencesManager

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.settings_title)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val prefAssignPitStops = findPreference<ListPreference>("assign_pitstop_dur_to")
        prefAssignPitStops?.summary = preferencesManager.pitStopAssign.name.toLowerCase(Locale.getDefault())
        prefAssignPitStops?.setOnPreferenceChangeListener { preference, newValue ->
            preference.summary = newValue.toString()
            true
        }

        val prefSessionButtonType = findPreference<ListPreference>("session_button_type")
        prefSessionButtonType?.summary = preferencesManager.sessionButtonType.toLowerCase(Locale.getDefault())
        prefSessionButtonType?.setOnPreferenceChangeListener { preference, newValue ->
            preference.summary = newValue.toString()
            prefAssignPitStops?.isEnabled = "pit".equals(newValue.toString(), ignoreCase = true)
            true
        }
        prefAssignPitStops?.isEnabled = "pit".equals(preferencesManager.sessionButtonType, ignoreCase = true)
    }
}