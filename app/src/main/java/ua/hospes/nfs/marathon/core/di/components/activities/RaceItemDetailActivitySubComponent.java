package ua.hospes.nfs.marathon.core.di.components.activities;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.hospes.nfs.marathon.core.di.module.DomainModule;
import ua.hospes.nfs.marathon.ui.race.detail.RaceItemDetailActivity;

/**
 * @author Andrew Khloponin
 */
@Subcomponent(modules = {DomainModule.class})
public interface RaceItemDetailActivitySubComponent extends AndroidInjector<RaceItemDetailActivity> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<RaceItemDetailActivity> {}
}