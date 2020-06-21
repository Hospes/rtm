package ua.hospes.rtm

import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : AppBase() {
    override fun provideTimberTree(): Timber.Tree = Timber.DebugTree()
}