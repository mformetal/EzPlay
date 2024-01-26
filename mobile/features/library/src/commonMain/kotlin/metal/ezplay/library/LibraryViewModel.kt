package metal.ezplay.library

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import metal.ezplay.network.EzPlayApi
import metal.ezplay.viewmodel.ViewModel

class LibraryViewModel(private val api: EzPlayApi) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryState())
    val uiState: StateFlow<LibraryState> = _uiState.asStateFlow()

    fun fetchLibrary() {
        viewModelScope.launch {
            val library = api.library()
            _uiState.update { state ->
                state.copy(artists = library)
            }
        }
    }
}