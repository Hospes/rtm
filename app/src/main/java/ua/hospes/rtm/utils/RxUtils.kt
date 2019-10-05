package ua.hospes.rtm.utils

object RxUtils {
    //    @JvmStatic
    //    fun <T> applySchedulers(): ObservableTransformer<T, T> =
    //            ObservableTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    //
    //    @JvmStatic
    //    fun <T> applySchedulersSingle(): SingleTransformer<T, T> =
    //            SingleTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    //
    //    @JvmStatic
    //    fun <T> applySchedulersMaybe(): MaybeTransformer<T, T> =
    //            MaybeTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    //
    //    @JvmStatic
    //    fun <T> applySchedulersFlowable(): FlowableTransformer<T, T> =
    //            FlowableTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    //
    //    @JvmStatic
    //    fun applySchedulersCompletable(): CompletableTransformer =
    //            CompletableTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
}

/**
 * disposable += observable.subscribe()
 */
//operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
//    add(disposable)
//}