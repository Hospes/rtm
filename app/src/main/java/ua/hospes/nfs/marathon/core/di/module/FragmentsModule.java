package ua.hospes.nfs.marathon.core.di.module;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import ua.hospes.nfs.marathon.core.di.components.fragments.AddTeamToRaceDialogFragmentSubComponent;
import ua.hospes.nfs.marathon.core.di.components.fragments.CarsFragmentSubComponent;
import ua.hospes.nfs.marathon.core.di.components.fragments.DriversFragmentSubComponent;
import ua.hospes.nfs.marathon.core.di.components.fragments.EditCarDialogFragmentSubComponent;
import ua.hospes.nfs.marathon.core.di.components.fragments.EditDriverDialogFragmentSubComponent;
import ua.hospes.nfs.marathon.core.di.components.fragments.EditTeamDialogFragmentSubComponent;
import ua.hospes.nfs.marathon.core.di.components.fragments.RaceFragmentSubComponent;
import ua.hospes.nfs.marathon.core.di.components.fragments.SetCarDialogFragmentSubComponent;
import ua.hospes.nfs.marathon.core.di.components.fragments.SetDriverDialogFragmentSubComponent;
import ua.hospes.nfs.marathon.core.di.components.fragments.SettingsFragmentSubComponent;
import ua.hospes.nfs.marathon.core.di.components.fragments.TeamsFragmentSubComponent;
import ua.hospes.nfs.marathon.ui.cars.CarsFragment;
import ua.hospes.nfs.marathon.ui.cars.EditCarDialogFragment;
import ua.hospes.nfs.marathon.ui.drivers.DriversFragment;
import ua.hospes.nfs.marathon.ui.drivers.EditDriverDialogFragment;
import ua.hospes.nfs.marathon.ui.race.AddTeamToRaceDialogFragment;
import ua.hospes.nfs.marathon.ui.race.RaceFragment;
import ua.hospes.nfs.marathon.ui.race.SetCarDialogFragment;
import ua.hospes.nfs.marathon.ui.race.SetDriverDialogFragment;
import ua.hospes.nfs.marathon.ui.settings.SettingsFragment;
import ua.hospes.nfs.marathon.ui.teams.EditTeamDialogFragment;
import ua.hospes.nfs.marathon.ui.teams.TeamsFragment;

/**
 * @author Andrew Khloponin
 */
@Module(subcomponents = {
        CarsFragmentSubComponent.class,
        EditCarDialogFragmentSubComponent.class,

        DriversFragmentSubComponent.class,
        EditDriverDialogFragmentSubComponent.class,

        AddTeamToRaceDialogFragmentSubComponent.class,

        RaceFragmentSubComponent.class,
        SetCarDialogFragmentSubComponent.class,
        SetDriverDialogFragmentSubComponent.class,

        SettingsFragmentSubComponent.class,

        TeamsFragmentSubComponent.class,
        EditTeamDialogFragmentSubComponent.class,
})
public abstract class FragmentsModule {
    @Binds
    @IntoMap
    @FragmentKey(CarsFragment.class)
    abstract AndroidInjector.Factory<? extends android.support.v4.app.Fragment> bindCarsFragmentInjectorFactory(CarsFragmentSubComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(EditCarDialogFragment.class)
    abstract AndroidInjector.Factory<? extends android.support.v4.app.Fragment> bindEditCarDialogFragmentInjectorFactory(EditCarDialogFragmentSubComponent.Builder builder);


    @Binds
    @IntoMap
    @FragmentKey(DriversFragment.class)
    abstract AndroidInjector.Factory<? extends android.support.v4.app.Fragment> bindDriversFragmentInjectorFactory(DriversFragmentSubComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(EditDriverDialogFragment.class)
    abstract AndroidInjector.Factory<? extends android.support.v4.app.Fragment> bindEditDriverDialogFragmentInjectorFactory(EditDriverDialogFragmentSubComponent.Builder builder);


    @Binds
    @IntoMap
    @FragmentKey(AddTeamToRaceDialogFragment.class)
    abstract AndroidInjector.Factory<? extends android.support.v4.app.Fragment> bindAddTeamToRaceDialogFragmentInjectorFactory(AddTeamToRaceDialogFragmentSubComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(RaceFragment.class)
    abstract AndroidInjector.Factory<? extends android.support.v4.app.Fragment> bindRaceFragmentInjectorFactory(RaceFragmentSubComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(SetCarDialogFragment.class)
    abstract AndroidInjector.Factory<? extends android.support.v4.app.Fragment> bindSetCarDialogFragmentInjectorFactory(SetCarDialogFragmentSubComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(SetDriverDialogFragment.class)
    abstract AndroidInjector.Factory<? extends android.support.v4.app.Fragment> bindSetDriverDialogFragmentInjectorFactory(SetDriverDialogFragmentSubComponent.Builder builder);


    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment.class)
    abstract AndroidInjector.Factory<? extends android.support.v4.app.Fragment> bindSettingsFragmentInjectorFactory(SettingsFragmentSubComponent.Builder builder);


    @Binds
    @IntoMap
    @FragmentKey(TeamsFragment.class)
    abstract AndroidInjector.Factory<? extends android.support.v4.app.Fragment> bindTeamsFragmentInjectorFactory(TeamsFragmentSubComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(EditTeamDialogFragment.class)
    abstract AndroidInjector.Factory<? extends android.support.v4.app.Fragment> bindEditTeamDialogFragmentInjectorFactory(EditTeamDialogFragmentSubComponent.Builder builder);

}