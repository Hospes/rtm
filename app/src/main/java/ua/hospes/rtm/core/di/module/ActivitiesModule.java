package ua.hospes.rtm.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ua.hospes.rtm.ui.MainActivity;
import ua.hospes.rtm.ui.race.detail.RaceItemDetailActivity;

/**
 * @author Andrew Khloponin
 */
@Module
public abstract class ActivitiesModule {
    @ContributesAndroidInjector(modules = {FragmentsModule.class})
    abstract MainActivity contributeMainActivityInjector();

    @ContributesAndroidInjector(modules = {DomainModule.class})
    abstract RaceItemDetailActivity contributeRaceItemDetailActivityInjector();
}