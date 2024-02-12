package metal.ezplay.multiplatform.coroutines

import kotlinx.coroutines.delay

suspend fun <T> retry(
    times: Int = Int.MAX_VALUE,
    initialDelay: Long = 100, // 0.1 second
    maxDelay: Long = 1000,    // 1 second
    factor: Double = 2.0,
    block: suspend () -> T): Result<T>
{
    var currentDelay = initialDelay
    repeat(times - 1) {
        runCatching {
            try {
                return runCatching { block() }
            } catch (e: Exception) {

            }
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return runCatching { block() }
}