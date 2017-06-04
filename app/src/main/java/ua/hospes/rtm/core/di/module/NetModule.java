package ua.hospes.rtm.core.di.module;

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
}