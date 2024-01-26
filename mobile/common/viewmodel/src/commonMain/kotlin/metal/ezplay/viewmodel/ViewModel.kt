package metal.ezplay.viewmodel

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

open class ViewModel(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    protected val viewModelScope: CoroutineScope = CoroutineScope(dispatcher)

    public open fun onCleared() {
        viewModelScope.cancel()
    }
}