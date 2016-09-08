package ua.hospes.nfs.marathon.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;

import hugo.weaving.DebugLog;

/**
 * @author Andrew Khloponin
 */
public abstract class StopWatchFragment extends Fragment implements StopWatch.OnStopWatchTickListener {
    private StopWatchService.StopWatchBinder binder;

    @Override
    public void onResume() {
        super.onResume();
        getContext().bindService(new Intent(getContext(), StopWatchService.class), connection, Context.BIND_IMPORTANT);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unbindService(connection);
        if (binder == null) return;
        binder.removeStopWatchListener(this);
        binder = null;
    }


    private ServiceConnection connection = new ServiceConnection() {
        @DebugLog
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = ((StopWatchService.StopWatchBinder) iBinder);
            binder.addStopWatchListener(StopWatchFragment.this);
        }

        @DebugLog
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (binder == null) return;
            binder.removeStopWatchListener(StopWatchFragment.this);
            binder = null;
        }
    };
}