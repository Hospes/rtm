package ua.hospes.rtm.core.di.components;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import ua.hospes.rtm.App;
import ua.hospes.rtm.core.di.module.ActivitiesModule;
import ua.hospes.rtm.core.di.module.AppModule;

/**
 * @author Andrew Khloponin
 */
@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, ActivitiesModule.class})
public interface AppComponent {
    void inject(App app);
}