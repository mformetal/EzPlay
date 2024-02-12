package metal.ezplay.multiplatform.coroutines

import kotlinx.coroutines.delay

suspend fun <T> retry(
    times: Int = 5,
    factor: Double = 2.0,
    block: suspend () -> T): Result<T>
{
    var currentDelay = 0L
    repeat(times - 1) { retryCount ->
        val result = runCatching {
            block()
        }
        if (result.isSuccess) {
            return result
        }

        delay(currentDelay)
        currentDelay = (retryCount * factor).toLong()
    }
    return runCatching { block() }
}