package ua.hospes.nfs.marathon.ui.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import javax.inject.Inject;

import autodagger.AutoInjector;
import hugo.weaving.DebugLog;
import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.di.Injector;
import ua.hospes.nfs.marathon.domain.preferences.PreferencesManager;
import ua.hospes.nfs.marathon.ui.MainActivity;
import ua.hospes.nfs.marathon.ui.MainActivityComponent;

/**
 * @author Andrew Khloponin
 */
@AutoInjector(MainActivity.class)
public class SettingsFragment extends PreferenceFragmentCompat {
    @Inject PreferencesManager preferencesManager;


    public static Fragment newInstance() {
        return new SettingsFragment();
    }


    @DebugLog
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.getComponent(getActivity(), MainActivityComponent.class).inject(this);
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