package lv.uni.ubiassignmentapplication.core.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend fun <T> callAsync(bgDispatcher: CoroutineDispatcher, block: suspend () -> T): T {
    return withContext(bgDispatcher) {
        block()
    }
}

suspend fun <T> runAsync(
    bgDispatcher: CoroutineDispatcher,
    silent: Boolean = false,
    block: suspend () -> T
): AsyncResult<T> {
    return try {
        val result = callAsync(bgDispatcher, block)
        AsyncResult(result, null, false)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        AsyncResult<T>(null, e, true).also { ret ->
            checkErrorInternal(ret, silent)
        }
    }
}

fun <T> checkErrorInternal(it: AsyncResult<T>, silent: Boolean) {
    if (it.isFailure) {
        val e = it.getError()
        Timber.e(e)
    }
}

class AsyncResult<T>(
    private val value: T?,
    private val error: Throwable?,
    val isFailure: Boolean
) {
    val isSuccess = !isFailure
    fun getValue() = value
    fun getResponse() = value ?: throw NullPointerException("No response: $this")
    fun getError() = error!!

    override fun toString(): String =
        "AsyncResult(value=$value, error=$error, isFailure=$isFailure)"
}