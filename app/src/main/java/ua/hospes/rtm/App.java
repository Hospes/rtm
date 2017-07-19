package ua.hospes.rtm;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.ftinc.scoop.Scoop;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.fabric.sdk.android.Fabric;
import ua.hospes.rtm.core.di.components.DaggerAppComponent;
import ua.hospes.rtm.core.di.module.AppModule;

/**
 * @author Andrew Khloponin
 */
public class App extends MultiDexApplication implements HasActivityInjector {
    @Inject DispatchingAndroidInjector<Activity> activityInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        Scoop.waffleCone()
                .addFlavor("Default", R.style.AppTheme, R.style.AppTheme_DialogWhenLarge, true)
                .addFlavor("Light", R.style.AppTheme_Light, R.style.AppTheme_Light_DialogWhenLarge)
                .setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this))
                .initialize();

        DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build().inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }
}