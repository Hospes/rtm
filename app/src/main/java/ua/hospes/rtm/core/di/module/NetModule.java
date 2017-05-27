package ua.hospes.rtm.core.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.hospes.rtm.domain.preferences.PreferencesManager;
import ua.hospes.rtm.domain.preferences.PreferencesManagerImpl;

/**
 * @author Andrew Khloponin
 */
@Module
public class NetModule {
    @Provides
    PreferencesManager providePreferencesManager(PreferencesManagerImpl manager) {
        return manager;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }
}