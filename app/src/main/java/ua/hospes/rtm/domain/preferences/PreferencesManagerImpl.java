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


    private static final String KEY_ASSIGN_PITSTOP = "assign_pitstop_dur_to";
    private static final String KEY_PITSTOP_SESSIONS_REMOVED = "remove_pit_stops";

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
    public boolean isPitStopSessionsRemoved() {
        return getBoolean(KEY_PITSTOP_SESSIONS_REMOVED, false);
    }
}