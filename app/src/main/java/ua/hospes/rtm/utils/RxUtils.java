package ua.hospes.rtm.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * @author Andrew Khloponin
 */
public class RxUtils {
    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressWarnings("unchecked")
    public static <T> SingleTransformer<T, T> applySchedulersSingle() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static <T, U> List<T> listMap(List<U> items, Function<U, T> mapper) throws Exception {
        List<T> result = new ArrayList<>();
        for (U item : items) result.add(mapper.apply(item));
        return result;
    }

    private static final HashMap<Object, CompositeDisposable> sSubscriptions = new HashMap<Object, CompositeDisposable>();

    public static void manage(Object tag, Disposable subscription) {
        CompositeDisposable subscriptions = sSubscriptions.get(tag);
        if (subscriptions == null) {
            subscriptions = new CompositeDisposable();
            sSubscriptions.put(tag, subscriptions);
        }

        subscriptions.add(subscription);
    }

    public static void unsubscribe(Object tag) {
        CompositeDisposable subscriptions = sSubscriptions.get(tag);
        if (subscriptions != null) {
            subscriptions.dispose();
            sSubscriptions.remove(tag);
        }
    }
}