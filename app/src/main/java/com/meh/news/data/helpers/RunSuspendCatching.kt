package com.meh.news.data.helpers

import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

// https://github.com/Kotlin/kotlinx.coroutines/issues/1814
suspend inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (throwable: Throwable) {
        if (throwable is CancellationException) {
            currentCoroutineContext().ensureActive()
        }
        Result.failure(throwable)
    }
}