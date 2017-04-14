package ua.hospes.nfs.marathon.core.di.components.fragments;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.hospes.nfs.marathon.core.di.module.DomainModule;
import ua.hospes.nfs.marathon.core.di.module.NetModule;
import ua.hospes.nfs.marathon.ui.race.RaceFragment;

/**
 * @author Andrew Khloponin
 */
@Subcomponent(modules = {NetModule.class, DomainModule.class})
public interface RaceFragmentSubComponent extends AndroidInjector<RaceFragment> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<RaceFragment> {}
}