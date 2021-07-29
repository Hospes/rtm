package ua.hospes.rtm

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import timber.log.Timber

abstract class AppBase : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(provideTimberTree())

        createNotificationChannel()
    }

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