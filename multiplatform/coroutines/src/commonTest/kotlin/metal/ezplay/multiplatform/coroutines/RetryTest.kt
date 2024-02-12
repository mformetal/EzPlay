package metal.ezplay.multiplatform.coroutines

import kotlinx.coroutines.test.runTest
import kotlin.math.max
import kotlin.test.Test
import kotlin.test.assertEquals

class RetryTest {

    @Test
    fun `should only retry once if immediate success`() = runTest {
        var invocations = 0

        val result = retry {
            invocations++
        }

        assertEquals(result.isSuccess, true)
        assertEquals(invocations, 1)
    }

    @Test
    fun `should retry a max amount of times`() = runTest {
        var invocations = 0
        val maxRetryCount = 10

        val result = retry(times = maxRetryCount) {
            invocations++
            throw IllegalStateException()
        }

        assertEquals(result.isFailure, true)
        assertEquals(invocations, maxRetryCount)
    }
}