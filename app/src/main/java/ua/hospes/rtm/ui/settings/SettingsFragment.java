package ua.hospes.rtm.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.ftinc.scoop.ui.ScoopSettingsActivity;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
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
        updateABTitle(getActivity(), R.string.settings_title);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Preference prefAssignPitStops = findPreference("assign_pitstop_dur_to");
        String currentValue = preferencesManager.getPitStopAssign().name().toLowerCase(Locale.getDefault());
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

        findPreference("theme").setOnPreferenceClickListener(preference -> {
            getActivity().startActivityForResult(ScoopSettingsActivity.createIntent(getContext()), 999);
            return false;
        });
    }

    protected void updateABTitle(Activity activity, @StringRes int title) {
        if (title == -1 || title == 0 || !(activity instanceof AppCompatActivity)) return;

        ActionBar ab = ((AppCompatActivity) activity).getSupportActionBar();
        if (ab != null) ab.setTitle(getString(title));
    }
}