package ua.hospes.rtm.utils

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.*

object RxUtils {
    @JvmStatic
    fun <T> applySchedulers(): ObservableTransformer<T, T> =
            ObservableTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }

    @JvmStatic
    fun <T> applySchedulersSingle(): SingleTransformer<T, T> =
            SingleTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }

    @JvmStatic
    fun <T> applySchedulersMaybe(): MaybeTransformer<T, T> =
            MaybeTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }

    @JvmStatic
    fun <T> applySchedulersFlowable(): FlowableTransformer<T, T> =
            FlowableTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }

    @JvmStatic
    fun applySchedulersCompletable(): CompletableTransformer =
            CompletableTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }


    @Throws(Exception::class) @JvmStatic
    fun <T, U> listMap(items: List<U>, mapper: Function<U, T>): List<T> {
        val result = ArrayList<T>()
        for (item in items) result.add(mapper.apply(item))
        return result
    }


    @JvmStatic
    private val sSubscriptions = HashMap<Any, CompositeDisposable>()

    @JvmStatic
    fun manage(tag: Any, subscription: Disposable) {
        sSubscriptions.getOrPut(tag, defaultValue = { CompositeDisposable() }).add(subscription)
    }

    @JvmStatic
    fun unsubscribe(tag: Any) {
        sSubscriptions[tag]?.apply { dispose() }
        sSubscriptions.remove(tag)
    }
}

/**
 * disposable += observable.subscribe()
 */
operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}