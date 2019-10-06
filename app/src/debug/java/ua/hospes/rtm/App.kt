package ua.hospes.rtm

import com.facebook.stetho.Stetho
import timber.log.Timber

class App : AppBase() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }

    override fun provideTimberTree(): Timber.Tree = Timber.DebugTree()
}