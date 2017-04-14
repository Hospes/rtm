package ua.hospes.nfs.marathon.core.di.components.fragments;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.hospes.nfs.marathon.core.di.module.DomainModule;
import ua.hospes.nfs.marathon.core.di.module.NetModule;
import ua.hospes.nfs.marathon.ui.teams.TeamsFragment;

/**
 * @author Andrew Khloponin
 */
@Subcomponent(modules = {NetModule.class, DomainModule.class})
public interface TeamsFragmentSubComponent extends AndroidInjector<TeamsFragment> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<TeamsFragment> {}
}