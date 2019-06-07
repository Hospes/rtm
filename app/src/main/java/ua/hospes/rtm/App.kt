package ua.hospes.rtm

import android.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.ftinc.scoop.Scoop
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

        Scoop.waffleCone()
                .addFlavor("Default", R.style.AppTheme, R.style.AppTheme_DialogWhenLarge, true)
                .addFlavor("Light", R.style.AppTheme_Light, R.style.AppTheme_Light_DialogWhenLarge)
                .setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this))
                .initialize()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.builder().create(this)
}