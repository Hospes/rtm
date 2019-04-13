package ua.hospes.rtm.core.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ua.hospes.rtm.App
import ua.hospes.rtm.core.di.module.ActivitiesModule
import ua.hospes.rtm.core.di.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivitiesModule::class])
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}