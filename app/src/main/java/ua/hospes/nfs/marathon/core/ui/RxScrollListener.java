package ua.hospes.nfs.marathon.core.ui;

import android.support.v7.widget.RecyclerView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author Andrew Khloponin
 */
public abstract class RxScrollListener extends RecyclerView.OnScrollListener {
    private final ScrollEventsEmitter emitter = new ScrollEventsEmitter();

    public RxScrollListener() {
        configure(Observable.create(emitter))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onActionCall);
    }


    protected abstract void onActionCall(RecyclerView recyclerView);

    protected abstract Observable<RecyclerView> configure(Observable<RecyclerView> initial);


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        emitter.emit(recyclerView);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            emitter.emit(recyclerView);
        }
    }


    public void forceUpdate(RecyclerView recyclerView) {
        emitter.emit(recyclerView);
    }

    private class ScrollEventsEmitter implements Observable.OnSubscribe<RecyclerView> {
        private Subscriber<? super RecyclerView> subscriber;

        @Override
        public void call(Subscriber<? super RecyclerView> subscriber) {
            this.subscriber = subscriber;
        }

        public void emit(RecyclerView recyclerView) {
            subscriber.onNext(recyclerView);
        }
    }
}