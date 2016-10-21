package ua.hospes.nfs.marathon.ui.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import ua.hospes.nfs.marathon.R;

/**
 * @author Andrew Khloponin
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    public static Fragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        Preference preference = findPreference("assign_pitstop_dur_to");
        String currentValue = preference.getPreferenceManager().getSharedPreferences().getString("assign_pitstop_dur_to", "manual");
        preference.setSummary(currentValue);
        preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(String.valueOf(newValue));
                return true;
            }
        });
    }
}