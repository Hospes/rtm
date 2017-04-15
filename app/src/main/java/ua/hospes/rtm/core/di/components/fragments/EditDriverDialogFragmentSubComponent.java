package ua.hospes.rtm.core.di.components.fragments;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.hospes.rtm.core.di.module.DomainModule;
import ua.hospes.rtm.ui.drivers.EditDriverDialogFragment;

/**
 * @author Andrew Khloponin
 */
@Subcomponent(modules = {DomainModule.class})
public interface EditDriverDialogFragmentSubComponent extends AndroidInjector<EditDriverDialogFragment> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<EditDriverDialogFragment> {}
}