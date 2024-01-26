package metal.ezplay.viewmodel

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

expect open class ViewModel(dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    val dispatcher: CoroutineDispatcher

    val viewModelScope: CoroutineScope

    open fun onCleared()
}
