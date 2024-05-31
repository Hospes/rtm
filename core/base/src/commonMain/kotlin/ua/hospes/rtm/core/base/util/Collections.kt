package ua.hospes.rtm.core.base.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
suspend fun <T> Collection<T>.parallelForEach(
    concurrency: Int = DEFAULT_CONCURRENCY,
    block: suspend (value: T) -> Unit,
) = asFlow()
    .flatMapMerge(concurrency = concurrency) { item ->
        flow {
            block(item)
            emit(Unit)
        }
    }
    .collect()
