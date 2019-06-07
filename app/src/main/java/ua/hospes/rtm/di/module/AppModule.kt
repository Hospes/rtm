package ua.hospes.rtm.di.module

import dagger.Module
import dagger.Provides
import ua.hospes.rtm.App

@Module
object AppModule {
    @Provides @JvmStatic
    internal fun provideContext(app: App) = app.applicationContext

    @Provides @JvmStatic
    internal fun providePackageManager(app: App) = app.packageManager
}