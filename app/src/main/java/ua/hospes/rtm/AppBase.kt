package ua.hospes.rtm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import ua.hospes.rtm.di.DaggerAppComponent

abstract class AppBase : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(provideTimberTree())

        createNotificationChannel()

        Fabric.with(this, Crashlytics.Builder().core(CrashlyticsCore.Builder().build()).build())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.builder().create(this)

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
    }

    abstract fun provideTimberTree(): Timber.Tree
}