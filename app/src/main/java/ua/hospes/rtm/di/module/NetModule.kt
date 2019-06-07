package ua.hospes.rtm.di.module

import dagger.Module
import dagger.Provides
import ua.hospes.rtm.domain.preferences.PreferencesManager
import ua.hospes.rtm.domain.preferences.PreferencesManagerImpl

@Module
object NetModule {
    @Provides @JvmStatic
    internal fun providePrefs(prefs: PreferencesManagerImpl): PreferencesManager = prefs
}