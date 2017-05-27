package ua.hospes.rtm.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ua.hospes.rtm.ui.cars.CarsFragment;
import ua.hospes.rtm.ui.cars.EditCarDialogFragment;
import ua.hospes.rtm.ui.drivers.DriversFragment;
import ua.hospes.rtm.ui.drivers.EditDriverDialogFragment;
import ua.hospes.rtm.ui.race.AddTeamToRaceDialogFragment;
import ua.hospes.rtm.ui.race.RaceFragment;
import ua.hospes.rtm.ui.race.SetCarDialogFragment;
import ua.hospes.rtm.ui.race.SetDriverDialogFragment;
import ua.hospes.rtm.ui.settings.SettingsFragment;
import ua.hospes.rtm.ui.teams.EditTeamDialogFragment;
import ua.hospes.rtm.ui.teams.SelectDriversDialogFragment;
import ua.hospes.rtm.ui.teams.TeamsFragment;

/**
 * @author Andrew Khloponin
 */
@Module(includes = {NetModule.class, DomainModule.class})
public abstract class FragmentsModule {
    @ContributesAndroidInjector
    abstract CarsFragment contributeCarsFragmentInjector();

    @ContributesAndroidInjector
    abstract EditCarDialogFragment contributeEditCarDialogFragmentInjectorFactory();

    @ContributesAndroidInjector
    abstract DriversFragment contributeDriversFragmentInjectorFactory();

    @ContributesAndroidInjector
    abstract EditDriverDialogFragment contributeEditDriverDialogFragmentInjectorFactory();

    @ContributesAndroidInjector
    abstract AddTeamToRaceDialogFragment contributeAddTeamToRaceDialogFragmentInjectorFactory();

    @ContributesAndroidInjector
    abstract RaceFragment contributeRaceFragmentInjectorFactory();

    @ContributesAndroidInjector
    abstract SetCarDialogFragment contributeSetCarDialogFragmentInjectorFactory();

    @ContributesAndroidInjector
    abstract SetDriverDialogFragment contributeSetDriverDialogFragmentInjectorFactory();

    @ContributesAndroidInjector
    abstract SettingsFragment contributeSettingsFragmentInjectorFactory();

    @ContributesAndroidInjector
    abstract TeamsFragment contributeTeamsFragmentInjectorFactory();

    @ContributesAndroidInjector
    abstract EditTeamDialogFragment contributeEditTeamDialogFragmentInjectorFactory();

    @ContributesAndroidInjector
    abstract SelectDriversDialogFragment contributeSelectDriversDialogFragmentInjectorFactory();
}