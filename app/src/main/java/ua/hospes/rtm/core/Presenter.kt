package ua.hospes.rtm.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.crashlytics.android.Crashlytics
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.*
import timber.log.Timber
import ua.hospes.rtm.utils.RxUtils
import ua.hospes.rtm.utils.plusAssign
import kotlin.coroutines.CoroutineContext

abstract class Presenter<V>(
        private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : CoroutineScope, LifecycleObserver {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + defaultDispatcher + exceptionHandler

    private val exceptionHandler = CoroutineExceptionHandler { _, t -> unexpectedErrors.onNext(t) }

    protected val disposables = CompositeDisposable()
    private val unexpectedErrors = PublishSubject.create<Throwable>()
    private val errors = PublishSubject.create<Throwable>()


    protected var view: V? = null


    open fun attachView(view: V?, lc: Lifecycle) {
        this.view = view
        lc.addObserver(this)

        disposables += errors.compose(RxUtils.applySchedulers())
                .doOnNext {
                    Timber.w(it)
                    Crashlytics.logException(it)
                }
                .subscribe(this::onError)

        disposables += unexpectedErrors.compose(RxUtils.applySchedulers())
                .doOnNext {
                    Timber.w(it)
                    Crashlytics.logException(it)
                }
                .subscribe(this::onUnexpectedError)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun detachView() {
        job.cancel()
        disposables.dispose()
        view = null
    }


    fun error(throwable: Throwable): Unit = errors.onNext(throwable)


    open fun onError(throwable: Throwable) {}

    open fun onUnexpectedError(throwable: Throwable) {}
}