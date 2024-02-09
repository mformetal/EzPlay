package metal.ezplay.viewmodel

import kotlinx.coroutines.CoroutineScope

expect open class MultiplatformViewModel {

    val scope: CoroutineScope
}