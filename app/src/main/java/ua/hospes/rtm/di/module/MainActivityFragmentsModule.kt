package ua.hospes.rtm.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ua.hospes.rtm.ui.cars.CarsFragment
import ua.hospes.rtm.ui.cars.EditCarDialogFragment
import ua.hospes.rtm.ui.drivers.DriversFragment
import ua.hospes.rtm.ui.drivers.EditDriverDialogFragment
import ua.hospes.rtm.ui.race.AddTeamToRaceDialogFragment
import ua.hospes.rtm.ui.race.RaceFragment
import ua.hospes.rtm.ui.race.SetCarDialogFragment
import ua.hospes.rtm.ui.race.SetDriverDialogFragment
import ua.hospes.rtm.ui.settings.SettingsFragment
import ua.hospes.rtm.ui.teams.EditTeamDialogFragment
import ua.hospes.rtm.ui.teams.SelectDriversDialogFragment
import ua.hospes.rtm.ui.teams.TeamsFragment

@Module
abstract class MainActivityFragmentsModule {
    @ContributesAndroidInjector
    internal abstract fun contributeCarsFragmentInjector(): CarsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeEditCarDialogFragmentInjectorFactory(): EditCarDialogFragment

    @ContributesAndroidInjector
    internal abstract fun contributeDriversFragmentInjectorFactory(): DriversFragment

    @ContributesAndroidInjector
    internal abstract fun contributeEditDriverDialogFragmentInjectorFactory(): EditDriverDialogFragment

    @ContributesAndroidInjector
    internal abstract fun contributeAddTeamToRaceDialogFragmentInjectorFactory(): AddTeamToRaceDialogFragment

    @ContributesAndroidInjector
    internal abstract fun contributeRaceFragmentInjectorFactory(): RaceFragment

    @ContributesAndroidInjector
    internal abstract fun contributeSetCarDialogFragmentInjectorFactory(): SetCarDialogFragment

    @ContributesAndroidInjector
    internal abstract fun contributeSetDriverDialogFragmentInjectorFactory(): SetDriverDialogFragment

    @ContributesAndroidInjector
    internal abstract fun contributeSettingsFragmentInjectorFactory(): SettingsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeTeamsFragmentInjectorFactory(): TeamsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeEditTeamDialogFragmentInjectorFactory(): EditTeamDialogFragment

    @ContributesAndroidInjector
    internal abstract fun contributeSelectDriversDialogFragmentInjectorFactory(): SelectDriversDialogFragment
}