package ua.hospes.nfs.marathon.domain.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import ua.hospes.nfs.marathon.domain.race.models.PitStopAssign;

@Singleton
public class PreferencesManagerImpl extends AbsPreferencesManager implements PreferencesManager {
    private SharedPreferences _preferences;

    @Inject
    public PreferencesManagerImpl(Context context) {
        _preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static final String KEY_ASSIGN_PITSTOP = "assign_pitstop_dur_to";

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
}