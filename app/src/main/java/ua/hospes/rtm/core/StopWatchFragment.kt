package ua.hospes.rtm.core

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

import ua.hospes.rtm.core.ui.AbsFragment

abstract class StopWatchFragment : AbsFragment, StopWatch.OnStopWatchListener {
    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    private var binder: StopWatchService.StopWatchBinder? = null

    protected val isStopWatchStarted: Boolean
        get() = binder?.getStopWatch()?.isStarted ?: false


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            binder = iBinder as StopWatchService.StopWatchBinder
            binder?.addStopWatchListener(this@StopWatchFragment)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            binder?.removeStopWatchListener(this@StopWatchFragment)?.also { binder = null }
        }
    }

    override fun onStart() {
        super.onStart()
        requireContext().bindService(Intent(context, StopWatchService::class.java), connection, Context.BIND_IMPORTANT)
    }

    override fun onStop() {
        super.onStop()
        requireContext().unbindService(connection)
        binder?.removeStopWatchListener(this)?.also { binder = null }
    }
}