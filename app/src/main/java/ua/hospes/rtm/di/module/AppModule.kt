package ua.hospes.rtm.di.module

import dagger.Module
import dagger.Provides
import ua.hospes.rtm.AppBase

@Module
object AppModule {
    @Provides @JvmStatic
    internal fun provideContext(app: AppBase) = app.applicationContext

    @Provides @JvmStatic
    internal fun providePackageManager(app: AppBase) = app.packageManager
}