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
import metal.ezplay.dto.SongDto
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
                state.copy(songs = library)
            }
        }
    }

    private suspend fun updateDatabase(library: List<SongDto>) {
        withContext(Dispatchers.IO) {
            appDatabase.artistQueries.transaction {
                library.forEach { song ->
                    appDatabase.artistQueries.insert(
                        song.id.toLong(),
                        song.name
                    )
                }
            }

            appDatabase.albumQueries.transaction {
                library.forEach { song ->
                    appDatabase.albumQueries.insert(
                        song.album.id.toLong(),
                        song.artist.id.toLong(),
                        song.album.name
                    )
                }
            }

            appDatabase.songQueries.transaction {
                library.forEach { song ->
                    appDatabase.songQueries.insert(
                        song.id.toLong(),
                        song.album.id.toLong(),
                        song.artist.id.toLong(),
                        song.name
                    )
                }
            }
        }
    }
}