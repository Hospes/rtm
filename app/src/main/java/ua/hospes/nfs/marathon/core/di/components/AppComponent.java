package ua.hospes.nfs.marathon.core.di.components;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import ua.hospes.nfs.marathon.App;
import ua.hospes.nfs.marathon.core.di.module.ActivitiesModule;
import ua.hospes.nfs.marathon.core.di.module.AppModule;

/**
 * @author Andrew Khloponin
 */
@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, ActivitiesModule.class})
public interface AppComponent {
    void inject(App app);
}