package ua.hospes.rtm.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ua.hospes.rtm.ui.MainActivity
import ua.hospes.rtm.ui.PrivacyActivity
import ua.hospes.rtm.ui.TermsActivity
import ua.hospes.rtm.ui.race.detail.RaceItemDetailActivity

@Module
abstract class ActivitiesModule {
    @ContributesAndroidInjector(modules = [MainActivityFragmentsModule::class])
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindRaceItemDetailActivity(): RaceItemDetailActivity


    @ContributesAndroidInjector
    internal abstract fun bindPrivacyActivity(): PrivacyActivity

    @ContributesAndroidInjector
    internal abstract fun bindTermsActivity(): TermsActivity
}
