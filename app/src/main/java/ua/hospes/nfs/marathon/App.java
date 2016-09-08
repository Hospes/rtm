package ua.hospes.nfs.marathon;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import javax.inject.Singleton;

import autodagger.AutoComponent;
import ua.hospes.nfs.marathon.core.di.module.ApplicationModule;
import ua.hospes.nfs.marathon.core.di.module.DataModule;
import ua.hospes.nfs.marathon.core.di.module.DbModule;
import ua.hospes.nfs.marathon.core.di.module.DomainModule;

/**
 * @author Andrew Khloponin
 */
@Singleton
@AutoComponent(modules = {ApplicationModule.class, DbModule.class, DomainModule.class, DataModule.class})
public class App extends MultiDexApplication {
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }



    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public AppComponent getComponent() {
        return component;
    }
}