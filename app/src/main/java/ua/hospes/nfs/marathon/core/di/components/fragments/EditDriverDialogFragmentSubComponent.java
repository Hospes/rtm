package ua.hospes.nfs.marathon.core.di.components.fragments;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.hospes.nfs.marathon.core.di.module.DomainModule;
import ua.hospes.nfs.marathon.ui.drivers.EditDriverDialogFragment;

/**
 * @author Andrew Khloponin
 */
@Subcomponent(modules = {DomainModule.class})
public interface EditDriverDialogFragmentSubComponent extends AndroidInjector<EditDriverDialogFragment> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<EditDriverDialogFragment> {}
}