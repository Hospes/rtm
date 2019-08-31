package ua.hospes.rtm.ui.settings

import android.app.Activity
import android.content.Context
import android.os.Bundle

import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import java.util.Locale

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.preferences.PreferencesManager

/**
 * @author Andrew Khloponin
 */
class SettingsFragment : PreferenceFragmentCompat() {
    @Inject
    internal var preferencesManager: PreferencesManager? = null


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        updateABTitle(activity, R.string.settings_title)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val prefAssignPitStops = findPreference("assign_pitstop_dur_to")
        prefAssignPitStops.summary = preferencesManager!!.pitStopAssign.name.toLowerCase(Locale.getDefault())
        prefAssignPitStops.setOnPreferenceChangeListener { preference, newValue ->
            preference.summary = newValue.toString()
            true
        }

        val prefSessionButtonType = findPreference("session_button_type")
        prefSessionButtonType.summary = preferencesManager!!.sessionButtonType.toLowerCase(Locale.getDefault())
        prefSessionButtonType.setOnPreferenceChangeListener { preference, newValue ->
            preference.summary = newValue.toString()
            prefAssignPitStops.isEnabled = "pit".equals(newValue.toString(), ignoreCase = true)
            true
        }
        prefAssignPitStops.isEnabled = "pit".equals(preferencesManager!!.sessionButtonType, ignoreCase = true)

        //        findPreference("theme").setOnPreferenceClickListener(preference -> {
        //            getActivity().startActivityForResult(ScoopSettingsActivity.createIntent(getContext()), 999);
        //            return false;
        //        });
    }

    protected fun updateABTitle(activity: Activity?, @StringRes title: Int) {
        if (title == -1 || title == 0 || activity !is AppCompatActivity) return

        val ab = activity.supportActionBar
        if (ab != null) ab.title = getString(title)
    }

    companion object {


        fun newInstance(): Fragment {
            return SettingsFragment()
        }
    }
}