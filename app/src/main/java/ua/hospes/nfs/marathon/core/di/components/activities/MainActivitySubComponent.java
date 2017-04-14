package ua.hospes.nfs.marathon.core.di.components.activities;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ua.hospes.nfs.marathon.core.di.module.FragmentsModule;
import ua.hospes.nfs.marathon.ui.MainActivity;

/**
 * @author Andrew Khloponin
 */
@Subcomponent(modules = {FragmentsModule.class})
public interface MainActivitySubComponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<MainActivity> {}
}