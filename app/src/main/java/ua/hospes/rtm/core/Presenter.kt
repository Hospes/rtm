package ua.hospes.rtm.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.crashlytics.android.Crashlytics
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

@Suppress("EXPERIMENTAL_API_USAGE")
abstract class Presenter<V>(
        private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Main
) : CoroutineScope, LifecycleObserver {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + defaultDispatcher + exceptionHandler

    private val exceptionHandler = CoroutineExceptionHandler { _, t -> unexpectedErrors.offer(t) }

    private val unexpectedErrors = BroadcastChannel<Throwable>(1)
    private val errors = BroadcastChannel<Throwable>(1)


    protected var view: V? = null


    open fun attachView(view: V?, lc: Lifecycle) {
        this.view = view
        lc.addObserver(this)

        launch {
            errors.consumeEach {
                Timber.w(it)
                Crashlytics.logException(it)
                withContext(Dispatchers.Main) { onError(it) }
            }
        }

        launch {
            unexpectedErrors.consumeEach {
                Timber.w(it)
                Crashlytics.logException(it)
                withContext(Dispatchers.Main) { onUnexpectedError(it) }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun detachView() {
        job.cancel()
        view = null
    }


    fun error(throwable: Throwable) = errors.offer(throwable).let { Unit }


    open fun onError(throwable: Throwable) {}

    open fun onUnexpectedError(throwable: Throwable) {}
}