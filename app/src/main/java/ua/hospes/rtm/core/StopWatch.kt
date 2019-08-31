package ua.hospes.rtm.core

import android.os.Handler
import android.os.Message
import java.util.*

private const val NANO_2_MILLIS = 1000000L
private const val TICK_DELAY = 100

// running states
private const val STATE_UNSTARTED = 0
private const val STATE_RUNNING = 1
private const val STATE_STOPPED = 2
private const val STATE_SUSPENDED = 3

// split state
private const val STATE_UNSPLIT = 10
private const val STATE_SPLIT = 11

private const val TICK_WHAT = 2

class StopWatch {
    /**
     * The current running state of the StopWatch.
     */
    private var runningState = STATE_UNSTARTED

    /**
     * Whether the stopwatch has a split time recorded.
     */
    private var splitState = STATE_UNSPLIT

    /**
     * The start time.
     */
    private var startTime: Long = 0

    /**
     * The start time in Millis - nanoTime is only for elapsed time so we
     * need to also store the currentTimeMillis to maintain the old
     * getStartTime API.
     */
    private var startTimeMillis: Long = 0

    /**
     * The stop time.
     */
    private var stopTime: Long = 0
    private var stopTimeMillis: Long = 0

    /**
     *
     *
     * Get the time on the stopwatch.
     *
     *
     *
     *
     * This is either the time between the start and the moment this method is called, or the amount of time between
     * start and stop.
     *
     *
     * @return the time in milliseconds
     */
    val time: Long
        get() = nanoTime / NANO_2_MILLIS

    /**
     *
     *
     * Get the time on the stopwatch in nanoseconds.
     *
     *
     *
     *
     * This is either the time between the start and the moment this method is called, or the amount of time between
     * start and stop.
     *
     *
     * @return the time in nanoseconds
     * @since 3.0
     */
    val nanoTime: Long
        get() {
            if (this.runningState == STATE_STOPPED || this.runningState == STATE_SUSPENDED) {
                return this.stopTime - this.startTime
            } else if (this.runningState == STATE_UNSTARTED) {
                return 0
            } else if (this.runningState == STATE_RUNNING) {
                return System.nanoTime() - this.startTime
            }
            throw RuntimeException("Illegal running state has occured. ")
        }

    /**
     *
     *
     * Get the split time on the stopwatch.
     *
     *
     *
     *
     * This is the time between start and latest split.
     *
     *
     * @return the split time in milliseconds
     * @throws IllegalStateException if the StopWatch has not yet been split.
     * @since 2.1
     */
    val splitTime: Long
        get() = splitNanoTime / NANO_2_MILLIS

    /**
     *
     *
     * Get the split time on the stopwatch in nanoseconds.
     *
     *
     *
     *
     * This is the time between start and latest split.
     *
     *
     * @return the split time in nanoseconds
     * @throws IllegalStateException if the StopWatch has not yet been split.
     * @since 3.0
     */
    val splitNanoTime: Long
        get() {
            check(this.splitState == STATE_SPLIT) { "Stopwatch must be split to get the split time. " }
            return this.stopTime - this.startTime
        }

    // System.nanoTime is for elapsed time
    val nanoStartTime: Long
        get() {
            check(this.runningState != STATE_UNSTARTED) { "Stopwatch has not been started" }
            return this.startTime
        }

    val isStarted: Boolean
        get() = runningState == STATE_RUNNING || runningState != STATE_UNSTARTED && runningState != STATE_STOPPED

    private val listeners = ArrayList<OnStopWatchListener>()

    private val mHandler = object : Handler() {
        override fun handleMessage(m: Message) {
            if (runningState == STATE_RUNNING) {
                dispatchStopWatchTick(time, nanoTime, System.nanoTime())
                sendMessageDelayed(Message.obtain(this, TICK_WHAT), TICK_DELAY.toLong())
            }
        }
    }


    /**
     *
     *
     * Start the stopwatch.
     *
     *
     *
     *
     * This method starts a new timing session, clearing any previous values.
     *
     *
     * @throws IllegalStateException if the StopWatch is already running.
     */
    fun start() {
        check(this.runningState != STATE_STOPPED) { "Stopwatch must be reset before being restarted. " }
        check(this.runningState == STATE_UNSTARTED) { "Stopwatch already started. " }
        this.startTime = System.nanoTime()
        this.startTimeMillis = System.currentTimeMillis()
        this.runningState = STATE_RUNNING
        dispatchStopWatchStarted(startTimeMillis, startTime)
        dispatchStopWatchStateChanged(runningState)
        updateRunning()
    }

    /**
     *
     *
     * Stop the stopwatch.
     *
     *
     *
     *
     * This method ends a new timing session, allowing the time to be retrieved.
     *
     *
     * @throws IllegalStateException if the StopWatch is not running.
     */
    fun stop() {
        check(!(this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED)) { "Stopwatch is not running. " }
        if (this.runningState == STATE_RUNNING) {
            this.stopTime = System.nanoTime()
            this.stopTimeMillis = System.currentTimeMillis()
        }
        this.runningState = STATE_STOPPED
        dispatchStopWatchStopped(stopTimeMillis, stopTime)
        dispatchStopWatchStateChanged(runningState)
        updateRunning()
    }

    /**
     *
     *
     * Resets the stopwatch. Stops it if need be.
     *
     *
     *
     *
     * This method clears the internal values to allow the object to be reused.
     *
     */
    fun reset() {
        this.runningState = STATE_UNSTARTED
        dispatchStopWatchStateChanged(runningState)
        this.splitState = STATE_UNSPLIT
    }

    /**
     *
     *
     * Split the time.
     *
     *
     *
     *
     * This method sets the stop time of the watch to allow a time to be extracted. The start time is unaffected,
     * enabling [.unsplit] to continue the timing from the original start point.
     *
     *
     * @throws IllegalStateException if the StopWatch is not running.
     */
    fun split() {
        check(this.runningState == STATE_RUNNING) { "Stopwatch is not running. " }
        this.stopTime = System.nanoTime()
        this.splitState = STATE_SPLIT
    }

    /**
     *
     *
     * Remove a split.
     *
     *
     *
     *
     * This method clears the stop time. The start time is unaffected, enabling timing from the original start point to
     * continue.
     *
     *
     * @throws IllegalStateException if the StopWatch has not been split.
     */
    fun unsplit() {
        check(this.splitState == STATE_SPLIT) { "Stopwatch has not been split. " }
        this.splitState = STATE_UNSPLIT
    }

    /**
     *
     *
     * Suspend the stopwatch for later resumption.
     *
     *
     *
     *
     * This method suspends the watch until it is resumed. The watch will not include time between the suspend and
     * resume calls in the total time.
     *
     *
     * @throws IllegalStateException if the StopWatch is not currently running.
     */
    fun suspend() {
        check(this.runningState == STATE_RUNNING) { "Stopwatch must be running to suspend. " }
        this.stopTime = System.nanoTime()
        this.runningState = STATE_SUSPENDED
        dispatchStopWatchStateChanged(runningState)
    }

    /**
     *
     *
     * Resume the stopwatch after a suspend.
     *
     *
     *
     *
     * This method resumes the watch after it was suspended. The watch will not include time between the suspend and
     * resume calls in the total time.
     *
     *
     * @throws IllegalStateException if the StopWatch has not been suspended.
     */
    fun resume() {
        check(this.runningState == STATE_SUSPENDED) { "Stopwatch must be suspended to resume. " }
        this.startTime += System.nanoTime() - this.stopTime
        this.runningState = STATE_RUNNING
        dispatchStopWatchStateChanged(runningState)
    }


    fun getTime(startTime: Long): Long {
        return getNanoTime(startTime) / NANO_2_MILLIS
    }

    fun getNanoTime(startTime: Long): Long {
        if (this.runningState == STATE_STOPPED || this.runningState == STATE_SUSPENDED) {
            return this.stopTime - startTime
        } else if (this.runningState == STATE_UNSTARTED) {
            return 0
        } else if (this.runningState == STATE_RUNNING) {
            return System.nanoTime() - startTime
        }
        throw RuntimeException("Illegal running state has occured. ")
    }

    /**
     * Returns the time this stopwatch was started.
     *
     * @return the time this stopwatch was started
     * @throws IllegalStateException if this StopWatch has not been started
     * @since 2.4
     */
    fun getStartTime(): Long {
        check(this.runningState != STATE_UNSTARTED) { "Stopwatch has not been started" }
        // System.nanoTime is for elapsed time
        return this.startTimeMillis
    }


    /**
     * A callback that notifies when the chronometer has incremented on its own.
     */
    interface OnStopWatchListener {
        fun onStopWatchStarted(startTime: Long, nanoStartTime: Long)

        fun onStopWatchStopped(stopTime: Long, nanoStopTime: Long)

        fun onStopWatchStateChanged(runningState: Int)

        /**
         * Notification that the chronometer has changed.
         */
        fun onStopWatchTick(time: Long, nanoTime: Long, currentNanoTime: Long)
    }

    fun addOnChronometerTickListener(listener: OnStopWatchListener?) {
        if (listeners.contains(listener) || listener == null) return
        listeners.add(listener)

        listener.onStopWatchStateChanged(runningState)
    }

    fun removeOnChronometerTickListener(listener: OnStopWatchListener) {
        listeners.remove(listener)
    }

    private fun updateRunning() {
        when (runningState) {
            STATE_RUNNING -> {
                dispatchStopWatchTick(time, nanoTime, System.nanoTime())
                mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), TICK_DELAY.toLong())
            }

            else -> mHandler.removeMessages(TICK_WHAT)
        }
    }

    private fun dispatchStopWatchStarted(startTime: Long, nanoStartTime: Long) {
        for (listener in listeners) listener.onStopWatchStarted(startTime, nanoStartTime)
    }

    private fun dispatchStopWatchStopped(stopTime: Long, nanoStopTime: Long) {
        for (listener in listeners) listener.onStopWatchStopped(stopTime, nanoStopTime)
    }

    private fun dispatchStopWatchStateChanged(runningState: Int) {
        for (listener in listeners) listener.onStopWatchStateChanged(runningState)
    }

    private fun dispatchStopWatchTick(time: Long, nanoTime: Long, currentNanoTime: Long) {
        for (listener in listeners) listener.onStopWatchTick(time, nanoTime, currentNanoTime)
    }
}