package ua.hospes.rtm.core.di.module

import dagger.Module
import dagger.Provides
import ua.hospes.rtm.App

@Module
object AppModule {
    @Provides @JvmStatic
    internal fun provideContext(app: App) = app.applicationContext

    @Provides @JvmStatic
    internal fun providePackageManager(app: App) = app.packageManager

    //    @Singleton
    //    @Provides @JvmStatic
    //    internal fun provideRxSchedulers(): AppRxSchedulers = AppRxSchedulers(
    //            io = Schedulers.io(),
    //            computation = Schedulers.computation(),
    //            main = AndroidSchedulers.mainThread()
    //    )
    //
    //    @Singleton
    //    @Provides @JvmStatic
    //    internal fun provideDispatchers(schedulers: AppRxSchedulers): AppCoroutineDispatchers = AppCoroutineDispatchers(
    //            io = schedulers.io.asCoroutineDispatcher(),
    //            computation = schedulers.computation.asCoroutineDispatcher(),
    //            main = Dispatchers.Main
    //    )
    //
    //    @Singleton
    //    @Provides @JvmStatic
    //    internal fun providePrefs(context: Context, moshi: Moshi): Prefs = Prefs(context, moshi)
}