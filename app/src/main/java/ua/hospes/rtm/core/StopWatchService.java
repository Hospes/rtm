package ua.hospes.rtm.core;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import ua.hospes.rtm.R;
import ua.hospes.rtm.ui.MainActivity;
import ua.hospes.rtm.utils.TimeUtils;

/**
 * @author Andrew Khloponin
 */
public class StopWatchService extends Service implements StopWatch.OnStopWatchListener {
    private static final int NOTIFICATION_ID = 1337;
    private static final String KEY_ACTION = "action";
    private static final String KEY_START = "start";
    private static final String KEY_STOP = "stop";
    private final IBinder binder = new StopWatchBinder();
    private final StopWatch stopWatch = new StopWatch();
    private NotificationManager mNotificationManager;
    private PowerManager.WakeLock wakeLock;


    public static void checkDeath(Context context) {
        context.startService(new Intent(context, StopWatchService.class));
    }

    public static void start(Context context) {
        context.startService(new Intent(context, StopWatchService.class).putExtra(KEY_ACTION, KEY_START));
    }

    public static void stop(Context context) {
        context.startService(new Intent(context, StopWatchService.class).putExtra(KEY_ACTION, KEY_STOP));
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @SuppressLint("WakelockTimeout")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra(KEY_ACTION)) {
            String action = intent.getStringExtra(KEY_ACTION);
            if (KEY_START.equals(action) && !stopWatch.isStarted()) {
                wakeLock.acquire();
                stopWatch.reset();
                stopWatch.start();
                startForeground(NOTIFICATION_ID, buildNotification(TimeUtils.format(stopWatch.getTime())));
                stopWatch.addOnChronometerTickListener(this);
                return START_STICKY;
            } else if (KEY_STOP.equals(action)) {
                stopWatch.stop();
                stopWatch.removeOnChronometerTickListener(this);
                stopForeground(true);
                if (wakeLock.isHeld()) wakeLock.release();
                return START_NOT_STICKY;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wakeLock.isHeld()) wakeLock.release();
    }

    private Notification buildNotification(String time) {
        Intent startMainActivity = new Intent(this, MainActivity.class);
        startMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, startMainActivity, 0);
        return new NotificationCompat.Builder(this)
                .setContentTitle("Marathon is running: " + time)
                .setContentIntent(pi)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .build();
    }


    @Override
    public void onStopWatchStarted(long startTime, long nanoStartTime) {}

    @Override
    public void onStopWatchStopped(long stopTime, long nanoStopTime) {}

    @Override
    public void onStopWatchStateChanged(int runningState) {}

    @Override
    public void onStopWatchTick(long time, long nanoTime, long currentNanoTime) {
        mNotificationManager.notify(1337, buildNotification(TimeUtils.format(stopWatch.getTime())));
    }


    class StopWatchBinder extends Binder {
        void addStopWatchListener(StopWatch.OnStopWatchListener listener) {
            stopWatch.addOnChronometerTickListener(listener);
        }

        void removeStopWatchListener(StopWatch.OnStopWatchListener listener) {
            stopWatch.removeOnChronometerTickListener(listener);
        }

        StopWatch getStopWatch() {
            return stopWatch;
        }
    }
}