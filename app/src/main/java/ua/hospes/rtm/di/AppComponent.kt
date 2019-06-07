package ua.hospes.rtm.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ua.hospes.rtm.App
import ua.hospes.rtm.di.module.ActivitiesModule
import ua.hospes.rtm.di.module.AppModule
import ua.hospes.rtm.di.module.NetModule
import ua.hospes.rtm.di.module.RepoModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class, NetModule::class, RepoModule::class,
    ActivitiesModule::class])
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}