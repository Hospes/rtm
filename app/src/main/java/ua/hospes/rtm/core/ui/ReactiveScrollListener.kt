package ua.hospes.rtm.core.ui

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

@Suppress("EXPERIMENTAL_API_USAGE")
abstract class ReactiveScrollListener : RecyclerView.OnScrollListener() {
    protected val subject = MutableSharedFlow<RecyclerView>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        subject.tryEmit(recyclerView)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            subject.tryEmit(recyclerView)
        }
    }


    fun forceUpdate(recyclerView: RecyclerView) {
        subject.tryEmit(recyclerView)
    }
}