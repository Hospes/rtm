package ua.hospes.rtm

import android.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.ftinc.scoop.Scoop
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.fabric.sdk.android.Fabric
import ua.hospes.rtm.core.di.DaggerAppComponent

class App : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()

        Fabric.with(this, Crashlytics())

        Scoop.waffleCone()
                .addFlavor("Default", R.style.AppTheme, R.style.AppTheme_DialogWhenLarge, true)
                .addFlavor("Light", R.style.AppTheme_Light, R.style.AppTheme_Light_DialogWhenLarge)
                .setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this))
                .initialize()

        //        DaggerAppComponent.builder()
        //                .appModule(AppModule(this))
        //                .build().inject(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.builder().create(this)
}