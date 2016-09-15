package ua.hospes.nfs.marathon.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @author Andrew Khloponin
 */
public class StopWatchService extends Service {
    private static final String KEY_ACTION = "action";
    private static final String KEY_START = "start";
    private static final String KEY_STOP = "stop";
    private final IBinder binder = new StopWatchBinder();
    private final StopWatch stopWatch = new StopWatch();


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
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra(KEY_ACTION)) {
            String action = intent.getStringExtra(KEY_ACTION);
            if (KEY_START.equals(action)) {
                stopWatch.reset();
                stopWatch.start();
                return START_STICKY;
            } else if (KEY_STOP.equals(action)) {
                stopWatch.stop();
                return START_NOT_STICKY;
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public class StopWatchBinder extends Binder {
        public void addStopWatchListener(StopWatch.OnStopWatchListener listener) {
            stopWatch.addOnChronometerTickListener(listener);
        }

        public void removeStopWatchListener(StopWatch.OnStopWatchListener listener) {
            stopWatch.removeOnChronometerTickListener(listener);
        }

        public StopWatch getStopWatch() {
            return stopWatch;
        }
    }
}