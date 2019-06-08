package ua.hospes.rtm

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import ua.hospes.rtm.di.DaggerAppComponent

class App : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        Fabric.with(this, Crashlytics.Builder().core(CrashlyticsCore.Builder().build()).build())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.builder().create(this)
}