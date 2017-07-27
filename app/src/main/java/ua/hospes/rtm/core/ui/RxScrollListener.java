package ua.hospes.rtm.core.ui;

import android.support.v7.widget.RecyclerView;

import io.reactivex.subjects.PublishSubject;

/**
 * @author Andrew Khloponin
 */
public abstract class RxScrollListener extends RecyclerView.OnScrollListener {
    protected final PublishSubject<RecyclerView> subject = PublishSubject.create();


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        subject.onNext(recyclerView);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            subject.onNext(recyclerView);
        }
    }


    public void forceUpdate(RecyclerView recyclerView) {
        subject.onNext(recyclerView);
    }
}