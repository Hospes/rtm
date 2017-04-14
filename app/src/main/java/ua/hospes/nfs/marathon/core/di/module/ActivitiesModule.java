package ua.hospes.nfs.marathon.core.di.module;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import ua.hospes.nfs.marathon.core.di.components.activities.MainActivitySubComponent;
import ua.hospes.nfs.marathon.core.di.components.activities.RaceItemDetailActivitySubComponent;
import ua.hospes.nfs.marathon.ui.MainActivity;
import ua.hospes.nfs.marathon.ui.race.detail.RaceItemDetailActivity;

/**
 * @author Andrew Khloponin
 */
@Module(subcomponents = {
        MainActivitySubComponent.class,
        RaceItemDetailActivitySubComponent.class
})
public abstract class ActivitiesModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivityInjectorFactory(MainActivitySubComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(RaceItemDetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindRaceItemDetailActivityInjectorFactory(RaceItemDetailActivitySubComponent.Builder builder);
}