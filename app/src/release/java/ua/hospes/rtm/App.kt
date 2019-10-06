package ua.hospes.rtm

import timber.log.Timber

class App : AppBase() {
    override fun provideTimberTree(): Timber.Tree = Timber.DebugTree()
}