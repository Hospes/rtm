package ua.hospes.nfs.marathon.core;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew Khloponin
 */
public class StopWatch {
    private static final String TAG = "StopWatch";
    private static final long NANO_2_MILLIS = 1000000L;
    private static final int TICK_DELAY = 100;

    // running states
    private static final int STATE_UNSTARTED = 0;
    private static final int STATE_RUNNING = 1;
    private static final int STATE_STOPPED = 2;
    private static final int STATE_SUSPENDED = 3;

    // split state
    private static final int STATE_UNSPLIT = 10;
    private static final int STATE_SPLIT = 11;

    /**
     * The current running state of the StopWatch.
     */
    private int runningState = STATE_UNSTARTED;

    /**
     * Whether the stopwatch has a split time recorded.
     */
    private int splitState = STATE_UNSPLIT;

    /**
     * The start time.
     */
    private long startTime;

    /**
     * The start time in Millis - nanoTime is only for elapsed time so we
     * need to also store the currentTimeMillis to maintain the old
     * getStartTime API.
     */
    private long startTimeMillis;

    /**
     * The stop time.
     */
    private long stopTime;


    public StopWatch() {}


    /**
     * <p>
     * Start the stopwatch.
     * </p>
     *
     * <p>
     * This method starts a new timing session, clearing any previous values.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is already running.
     */
    public void start() {
        if (this.runningState == STATE_STOPPED) {
            throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
        }
        if (this.runningState != STATE_UNSTARTED) {
            throw new IllegalStateException("Stopwatch already started. ");
        }
        this.startTime = System.nanoTime();
        this.startTimeMillis = System.currentTimeMillis();
        this.runningState = STATE_RUNNING;
        dispatchStopWatchStateChanged();
        updateRunning();
    }

    /**
     * <p>
     * Stop the stopwatch.
     * </p>
     *
     * <p>
     * This method ends a new timing session, allowing the time to be retrieved.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is not running.
     */
    public void stop() {
        if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
            throw new IllegalStateException("Stopwatch is not running. ");
        }
        if (this.runningState == STATE_RUNNING) {
            this.stopTime = System.nanoTime();
        }
        this.runningState = STATE_STOPPED;
        dispatchStopWatchStateChanged();
        updateRunning();
    }

    /**
     * <p>
     * Resets the stopwatch. Stops it if need be.
     * </p>
     *
     * <p>
     * This method clears the internal values to allow the object to be reused.
     * </p>
     */
    public void reset() {
        this.runningState = STATE_UNSTARTED;
        dispatchStopWatchStateChanged();
        this.splitState = STATE_UNSPLIT;
    }

    /**
     * <p>
     * Split the time.
     * </p>
     *
     * <p>
     * This method sets the stop time of the watch to allow a time to be extracted. The start time is unaffected,
     * enabling {@link #unsplit()} to continue the timing from the original start point.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is not running.
     */
    public void split() {
        if (this.runningState != STATE_RUNNING) {
            throw new IllegalStateException("Stopwatch is not running. ");
        }
        this.stopTime = System.nanoTime();
        this.splitState = STATE_SPLIT;
    }

    /**
     * <p>
     * Remove a split.
     * </p>
     *
     * <p>
     * This method clears the stop time. The start time is unaffected, enabling timing from the original start point to
     * continue.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch has not been split.
     */
    public void unsplit() {
        if (this.splitState != STATE_SPLIT) {
            throw new IllegalStateException("Stopwatch has not been split. ");
        }
        this.splitState = STATE_UNSPLIT;
    }

    /**
     * <p>
     * Suspend the stopwatch for later resumption.
     * </p>
     *
     * <p>
     * This method suspends the watch until it is resumed. The watch will not include time between the suspend and
     * resume calls in the total time.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is not currently running.
     */
    public void suspend() {
        if (this.runningState != STATE_RUNNING) {
            throw new IllegalStateException("Stopwatch must be running to suspend. ");
        }
        this.stopTime = System.nanoTime();
        this.runningState = STATE_SUSPENDED;
        dispatchStopWatchStateChanged();
    }

    /**
     * <p>
     * Resume the stopwatch after a suspend.
     * </p>
     *
     * <p>
     * This method resumes the watch after it was suspended. The watch will not include time between the suspend and
     * resume calls in the total time.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch has not been suspended.
     */
    public void resume() {
        if (this.runningState != STATE_SUSPENDED) {
            throw new IllegalStateException("Stopwatch must be suspended to resume. ");
        }
        this.startTime += (System.nanoTime() - this.stopTime);
        this.runningState = STATE_RUNNING;
        dispatchStopWatchStateChanged();
    }

    /**
     * <p>
     * Get the time on the stopwatch.
     * </p>
     *
     * <p>
     * This is either the time between the start and the moment this method is called, or the amount of time between
     * start and stop.
     * </p>
     *
     * @return the time in milliseconds
     */
    public long getTime() {
        return getNanoTime() / NANO_2_MILLIS;
    }

    /**
     * <p>
     * Get the time on the stopwatch in nanoseconds.
     * </p>
     *
     * <p>
     * This is either the time between the start and the moment this method is called, or the amount of time between
     * start and stop.
     * </p>
     *
     * @return the time in nanoseconds
     * @since 3.0
     */
    public long getNanoTime() {
        if (this.runningState == STATE_STOPPED || this.runningState == STATE_SUSPENDED) {
            return this.stopTime - this.startTime;
        } else if (this.runningState == STATE_UNSTARTED) {
            return 0;
        } else if (this.runningState == STATE_RUNNING) {
            return System.nanoTime() - this.startTime;
        }
        throw new RuntimeException("Illegal running state has occured. ");
    }

    /**
     * <p>
     * Get the split time on the stopwatch.
     * </p>
     *
     * <p>
     * This is the time between start and latest split.
     * </p>
     *
     * @return the split time in milliseconds
     * @throws IllegalStateException if the StopWatch has not yet been split.
     * @since 2.1
     */
    public long getSplitTime() {
        return getSplitNanoTime() / NANO_2_MILLIS;
    }

    /**
     * <p>
     * Get the split time on the stopwatch in nanoseconds.
     * </p>
     *
     * <p>
     * This is the time between start and latest split.
     * </p>
     *
     * @return the split time in nanoseconds
     * @throws IllegalStateException if the StopWatch has not yet been split.
     * @since 3.0
     */
    public long getSplitNanoTime() {
        if (this.splitState != STATE_SPLIT) {
            throw new IllegalStateException("Stopwatch must be split to get the split time. ");
        }
        return this.stopTime - this.startTime;
    }

    /**
     * Returns the time this stopwatch was started.
     *
     * @return the time this stopwatch was started
     * @throws IllegalStateException if this StopWatch has not been started
     * @since 2.4
     */
    public long getStartTime() {
        if (this.runningState == STATE_UNSTARTED) {
            throw new IllegalStateException("Stopwatch has not been started");
        }
        // System.nanoTime is for elapsed time
        return this.startTimeMillis;
    }

    public boolean isStarted() {
        return runningState == STATE_RUNNING || (runningState != STATE_UNSTARTED && runningState != STATE_STOPPED);
    }


    /**
     * A callback that notifies when the chronometer has incremented on its own.
     */
    public interface OnStopWatchListener {
        void onStopWatchStateChanged(StopWatch stopWatch);

        /**
         * Notification that the chronometer has changed.
         */
        void onStopWatchTick(StopWatch stopWatch);
    }

    private final List<OnStopWatchListener> listeners = new ArrayList<>();

    private static final int TICK_WHAT = 2;

    public void addOnChronometerTickListener(OnStopWatchListener listener) {
        if (listeners.contains(listener) || listener == null) return;
        listeners.add(listener);

        listener.onStopWatchStateChanged(this);
    }

    public void removeOnChronometerTickListener(OnStopWatchListener listener) {
        listeners.remove(listener);
    }

    private void updateRunning() {
        switch (runningState) {
            case STATE_RUNNING:
                dispatchStopWatchTick();
                mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), TICK_DELAY);
                break;

            default:
                mHandler.removeMessages(TICK_WHAT);
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (runningState == STATE_RUNNING) {
                dispatchStopWatchTick();
                sendMessageDelayed(Message.obtain(this, TICK_WHAT), TICK_DELAY);
            }
        }
    };

    private void dispatchStopWatchStateChanged() {
        for (OnStopWatchListener listener : listeners) listener.onStopWatchStateChanged(this);
    }

    private void dispatchStopWatchTick() {
        for (OnStopWatchListener listener : listeners) {
            listener.onStopWatchTick(this);
        }
    }
}