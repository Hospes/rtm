package ua.hospes.rtm.core

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import ua.hospes.rtm.BuildConfig

import ua.hospes.rtm.R
import ua.hospes.rtm.ui.MainActivity
import ua.hospes.rtm.utils.TimeUtils

private const val NOTIFICATION_ID = 1337
private const val KEY_ACTION = "action"
private const val KEY_START = "start"
private const val KEY_STOP = "stop"

class StopWatchService : Service(), StopWatch.OnStopWatchListener {
    private val binder = StopWatchBinder()
    private val stopWatch = StopWatch()
    private val mNotificationManager: NotificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val wakeLock: PowerManager.WakeLock by lazy {
        val mgr = getSystemService(Context.POWER_SERVICE) as PowerManager
        mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ua.hospes.rtm:MyWakeLock")
    }


    override fun onBind(intent: Intent): IBinder? = binder

    @SuppressLint("WakelockTimeout")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = when (intent?.getStringExtra(KEY_ACTION)) {
        KEY_START -> {
            if (!stopWatch.isStarted) {
                wakeLock.acquire()
                stopWatch.reset()
                stopWatch.start()
                startForeground(NOTIFICATION_ID, buildNotification(TimeUtils.format(stopWatch.time)))
                stopWatch.addOnChronometerTickListener(this)
            }
            START_STICKY
        }

        KEY_STOP -> {
            stopWatch.stop()
            stopWatch.removeOnChronometerTickListener(this)
            stopForeground(true)
            if (wakeLock.isHeld) wakeLock.release()
            START_NOT_STICKY
        }

        else -> START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (wakeLock.isHeld) wakeLock.release()
    }

    private fun buildNotification(time: String): Notification {
        val startMainActivity = Intent(this, MainActivity::class.java)
            .apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP }
        val pi = PendingIntent.getActivity(this, 0, startMainActivity, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(this, BuildConfig.NOTIFICATION_CHANNEL_RACE)
            .setContentTitle("Marathon is running: $time")
            .setContentIntent(pi)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }


    override fun onStopWatchStarted(startTime: Long, nanoStartTime: Long) {}

    override fun onStopWatchStopped(stopTime: Long, nanoStopTime: Long) {}

    override fun onStopWatchStateChanged(runningState: Int) {}

    override fun onStopWatchTick(time: Long, nanoTime: Long, currentNanoTime: Long) {
        mNotificationManager.notify(NOTIFICATION_ID, buildNotification(TimeUtils.format(stopWatch.time)))
    }


    internal inner class StopWatchBinder : Binder() {
        fun addStopWatchListener(listener: StopWatch.OnStopWatchListener) {
            stopWatch.addOnChronometerTickListener(listener)
        }

        fun removeStopWatchListener(listener: StopWatch.OnStopWatchListener) {
            stopWatch.removeOnChronometerTickListener(listener)
        }

        fun getStopWatch(): StopWatch = this@StopWatchService.stopWatch
    }

    companion object {
        @JvmStatic
        fun checkDeath(context: Context) =
            context.startService(Intent(context, StopWatchService::class.java)).let { Unit }

        @JvmStatic
        fun start(context: Context) =
            context.startService(Intent(context, StopWatchService::class.java).putExtra(KEY_ACTION, KEY_START)).let { Unit }

        @JvmStatic
        fun stop(context: Context) =
            context.startService(Intent(context, StopWatchService::class.java).putExtra(KEY_ACTION, KEY_STOP)).let { Unit }
    }
}