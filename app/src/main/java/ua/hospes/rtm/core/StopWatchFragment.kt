package ua.hospes.rtm.core

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

import ua.hospes.rtm.core.ui.AbsFragment

abstract class StopWatchFragment : AbsFragment(), StopWatch.OnStopWatchListener {
    private var binder: StopWatchService.StopWatchBinder? = null

    protected val isStopWatchStarted: Boolean
        get() = binder != null && binder!!.stopWatch.isStarted


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            binder = iBinder as StopWatchService.StopWatchBinder
            binder!!.addStopWatchListener(this@StopWatchFragment)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            if (binder == null) return
            binder!!.removeStopWatchListener(this@StopWatchFragment)
            binder = null
        }
    }

    override fun onStart() {
        super.onStart()
        context!!.bindService(Intent(context, StopWatchService::class.java), connection, Context.BIND_IMPORTANT)
    }

    override fun onStop() {
        super.onStop()
        context!!.unbindService(connection)
        if (binder == null) return
        binder!!.removeStopWatchListener(this)
        binder = null
    }
}