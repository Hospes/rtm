package ua.hospes.rtm.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import ua.hospes.rtm.core.ui.AbsFragment;

/**
 * @author Andrew Khloponin
 */
public abstract class StopWatchFragment extends AbsFragment implements StopWatch.OnStopWatchListener {
    private StopWatchService.StopWatchBinder binder;

    @Override
    public void onStart() {
        super.onStart();
        bindToService();
    }

    protected void bindToService() {
        getContext().bindService(new Intent(getContext(), StopWatchService.class), connection, Context.BIND_IMPORTANT);
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unbindService(connection);
        if (binder == null) return;
        binder.removeStopWatchListener(this);
        binder = null;
    }

    protected boolean isStopWatchStarted() {
        return binder != null && binder.getStopWatch().isStarted();
    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = ((StopWatchService.StopWatchBinder) iBinder);
            binder.addStopWatchListener(StopWatchFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (binder == null) return;
            binder.removeStopWatchListener(StopWatchFragment.this);
            binder = null;
        }
    };
}