package ua.hospes.nfs.marathon.core.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.hospes.nfs.marathon.domain.preferences.PreferencesManager;
import ua.hospes.nfs.marathon.domain.preferences.PreferencesManagerImpl;

/**
 * @author Andrew Khloponin
 */
@Module
public class NetModule {
    @Provides
    public PreferencesManager providePreferencesManager(PreferencesManagerImpl manager) {
        return manager;
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder().create();
    }
}