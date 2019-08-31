package ua.hospes.rtm.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ua.hospes.rtm.domain.preferences.PreferencesManager
import ua.hospes.rtm.domain.preferences.PreferencesManagerImpl
import javax.inject.Singleton

@Module
object NetModule {
    @Singleton
    @Provides @JvmStatic
    internal fun providePrefs(ctx: Context): PreferencesManager = PreferencesManagerImpl(ctx)
}