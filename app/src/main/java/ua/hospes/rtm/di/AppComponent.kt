package ua.hospes.rtm.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ua.hospes.rtm.AppBase
import ua.hospes.rtm.di.module.*
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class, DbModule::class, NetModule::class, RepoModule::class,
    ActivitiesModule::class])
interface AppComponent : AndroidInjector<AppBase> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<AppBase>()
}