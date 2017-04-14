package ua.hospes.nfs.marathon;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasDispatchingActivityInjector;
import io.fabric.sdk.android.Fabric;
import ua.hospes.nfs.marathon.core.di.components.DaggerAppComponent;
import ua.hospes.nfs.marathon.core.di.module.AppModule;

/**
 * @author Andrew Khloponin
 */
public class App extends MultiDexApplication implements HasDispatchingActivityInjector {
    @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build().inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }
}