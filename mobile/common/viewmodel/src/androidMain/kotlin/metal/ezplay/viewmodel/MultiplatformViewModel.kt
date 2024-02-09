package metal.ezplay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual open class MultiplatformViewModel : ViewModel() {

    actual val scope: CoroutineScope = viewModelScope

}