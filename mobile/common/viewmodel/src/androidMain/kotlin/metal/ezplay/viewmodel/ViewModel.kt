package metal.ezplay.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

actual open class ViewModel actual constructor(actual val dispatcher: CoroutineDispatcher) : ViewModel() {

    actual val viewModelScope: CoroutineScope = CoroutineScope(dispatcher)

    public actual override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }
}
