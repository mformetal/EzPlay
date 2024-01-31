package metal.ezplay.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import metal.ezplay.dto.ArtistDto
import metal.ezplay.network.EzPlayApi
import metal.ezplay.storage.AppDatabase

class LibraryViewModel(private val api: EzPlayApi,
    private val appDatabase: AppDatabase) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryState())
    val uiState: StateFlow<LibraryState> = _uiState.asStateFlow()

    fun fetchLibrary() {
        viewModelScope.launch {
            val library = api.library()
            updateDatabase(library)

            _uiState.update { state ->
                state.copy(artists = library)
            }
        }
    }

    private suspend fun updateDatabase(library: List<ArtistDto>) {
        withContext(Dispatchers.IO) {
            appDatabase.songQueries.transaction {
                library.flatMap { it.albums }
                    .flatMap { it.songs }
                    .forEach {
                        appDatabase.songQueries.insert(
                            it.id.toLong(),
                            it.name
                        )
                    }
            }
        }
    }
}