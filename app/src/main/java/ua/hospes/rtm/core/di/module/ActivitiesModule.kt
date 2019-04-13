package ua.hospes.rtm.core.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ua.hospes.rtm.ui.MainActivity
import ua.hospes.rtm.ui.race.detail.RaceItemDetailActivity

@Suppress("unused")
@Module
abstract class ActivitiesModule {
    @ContributesAndroidInjector(modules = [FragmentsModule::class])
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [DomainModule::class])
    internal abstract fun bindRaceItemDetailActivity(): RaceItemDetailActivity
}
