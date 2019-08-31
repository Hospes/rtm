package ua.hospes.rtm.core.ui

import androidx.recyclerview.widget.RecyclerView

import io.reactivex.subjects.PublishSubject

/**
 * @author Andrew Khloponin
 */
abstract class RxScrollListener : RecyclerView.OnScrollListener() {
    protected val subject = PublishSubject.create<RecyclerView>()


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        subject.onNext(recyclerView)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            subject.onNext(recyclerView)
        }
    }


    fun forceUpdate(recyclerView: RecyclerView) {
        subject.onNext(recyclerView)
    }
}