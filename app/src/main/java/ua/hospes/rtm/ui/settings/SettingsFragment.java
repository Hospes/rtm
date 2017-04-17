package ua.hospes.rtm.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import hugo.weaving.DebugLog;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.preferences.PreferencesManager;

/**
 * @author Andrew Khloponin
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Inject PreferencesManager preferencesManager;


    public static Fragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @DebugLog
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @DebugLog
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Preference prefAssignPitStops = findPreference("assign_pitstop_dur_to");
        String currentValue = preferencesManager.getPitStopAssign().name().toLowerCase();
        prefAssignPitStops.setSummary(currentValue);
        prefAssignPitStops.setOnPreferenceChangeListener((preference, newValue) -> {
            preference.setSummary(String.valueOf(newValue));
            return true;
        });
        prefAssignPitStops.setEnabled(!preferencesManager.isPitStopSessionsRemoved());

        Preference prefIsPitStopsRemoved = findPreference("remove_pit_stops");
        prefIsPitStopsRemoved.setOnPreferenceChangeListener((preference, newValue) -> {
            prefAssignPitStops.setEnabled(!((boolean) newValue));
            return true;
        });
    }
}