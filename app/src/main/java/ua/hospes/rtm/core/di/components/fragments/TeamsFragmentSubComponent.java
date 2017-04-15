package ua.hospes.rtm.core.di.components.fragments;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.hospes.rtm.core.di.module.DomainModule;
import ua.hospes.rtm.core.di.module.NetModule;
import ua.hospes.rtm.ui.teams.TeamsFragment;

/**
 * @author Andrew Khloponin
 */
@Subcomponent(modules = {NetModule.class, DomainModule.class})
public interface TeamsFragmentSubComponent extends AndroidInjector<TeamsFragment> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<TeamsFragment> {}
}