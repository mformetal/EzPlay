package metal.ezplay.viewmodel

import kotlinx.coroutines.CoroutineScope

expect open class MultiplatformViewModel constructor() {

    val scope: CoroutineScope
}