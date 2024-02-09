package metal.ezplay.multiplatform.extensions

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AnyExtensionsTest {

    @Test
    fun `should return null if receiver is not instance of type T`() {
        val receiver = 0

        val instance = receiver.takeIfInstance<String>()

        assertNull(instance)
    }

    @Test
    fun `should return non-null if receiver is instance of type T`() {
        val receiver = 0

        val instance = receiver.takeIfInstance<Int>()

        assertNotNull(instance)
    }
}