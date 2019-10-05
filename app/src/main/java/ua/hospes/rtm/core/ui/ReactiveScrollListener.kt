package ua.hospes.rtm.core.ui

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.BroadcastChannel

@Suppress("EXPERIMENTAL_API_USAGE")
abstract class ReactiveScrollListener : RecyclerView.OnScrollListener() {
    protected val subject = BroadcastChannel<RecyclerView>(1)


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        subject.offer(recyclerView)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            subject.offer(recyclerView)
        }
    }


    fun forceUpdate(recyclerView: RecyclerView) {
        subject.offer(recyclerView)
    }
}