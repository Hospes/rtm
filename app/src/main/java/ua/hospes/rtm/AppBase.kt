package ua.hospes.rtm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import ua.hospes.rtm.di.DaggerAppComponent

abstract class AppBase : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(provideTimberTree())

        createNotificationChannel()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.builder().create(this)

    private fun createNotificationChannel() {
        val name = BuildConfig.NOTIFICATION_CHANNEL_RACE
        val descriptionText = "Notification for race progress visibility"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(BuildConfig.NOTIFICATION_CHANNEL_RACE, name, importance)
                .apply {
                    description = descriptionText
                }
        // Register the channel with the system
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    abstract fun provideTimberTree(): Timber.Tree
}