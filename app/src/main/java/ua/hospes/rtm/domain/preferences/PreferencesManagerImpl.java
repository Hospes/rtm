package ua.hospes.rtm.domain.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import ua.hospes.rtm.domain.race.models.PitStopAssign;

@Singleton
public class PreferencesManagerImpl extends AbsPreferencesManager implements PreferencesManager {
    private SharedPreferences _preferences;

    @Inject
    PreferencesManagerImpl(Context context) {
        _preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    private static final String KEY_ASSIGN_PITSTOP      = "assign_pitstop_dur_to";
    private static final String KEY_SESSION_BUTTON_TYPE = "session_button_type";
    private static final String KEY_ENABLE_EXPORT_XLS   = "enable_export_xls";

    @NonNull
    @Override
    public SharedPreferences getPreferences() {
        return _preferences;
    }


    //Additional functionality
    @Override
    public PitStopAssign getPitStopAssign() {
        try {
            return PitStopAssign.fromString(getString(KEY_ASSIGN_PITSTOP, "manual"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return PitStopAssign.MANUAL;
        }
    }

    @Override
    public String getSessionButtonType() {
        return getString(KEY_SESSION_BUTTON_TYPE, "pit");
    }

    @Override
    public boolean isExportXLSEnabled() {
        return getBoolean(KEY_ENABLE_EXPORT_XLS, false);
    }
}