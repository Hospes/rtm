package ua.hospes.rtm.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.hospes.rtm.R;
import ua.hospes.rtm.domain.preferences.PreferencesManager;

/**
 * @author Andrew Khloponin
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Inject
    PreferencesManager preferencesManager;


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
        prefAssignPitStops.setSummary(preferencesManager.getPitStopAssign().name().toLowerCase(Locale.getDefault()));
        prefAssignPitStops.setOnPreferenceChangeListener((preference, newValue) -> {
            preference.setSummary(String.valueOf(newValue));
            return true;
        });

        Preference prefSessionButtonType = findPreference("session_button_type");
        prefSessionButtonType.setSummary(preferencesManager.getSessionButtonType().toLowerCase(Locale.getDefault()));
        prefSessionButtonType.setOnPreferenceChangeListener((preference, newValue) -> {
            preference.setSummary(String.valueOf(newValue));
            prefAssignPitStops.setEnabled("pit".equalsIgnoreCase(String.valueOf(newValue)));
            return true;
        });
        prefAssignPitStops.setEnabled("pit".equalsIgnoreCase(preferencesManager.getSessionButtonType()));

//        findPreference("theme").setOnPreferenceClickListener(preference -> {
//            getActivity().startActivityForResult(ScoopSettingsActivity.createIntent(getContext()), 999);
//            return false;
//        });
    }

    protected void updateABTitle(Activity activity, @StringRes int title) {
        if (title == -1 || title == 0 || !(activity instanceof AppCompatActivity)) return;

        ActionBar ab = ((AppCompatActivity) activity).getSupportActionBar();
        if (ab != null) ab.setTitle(getString(title));
    }
}