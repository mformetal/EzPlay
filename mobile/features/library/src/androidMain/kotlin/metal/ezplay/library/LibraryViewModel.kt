package metal.ezplay.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.multiplatform.extensions.takeIfInstance
import metal.ezplay.network.Routes
import metal.ezplay.player.PlayerQueue
import metal.ezplay.storage.AppDatabase

class LibraryViewModel(private val client: HttpClient,
    private val appDatabase: AppDatabase,
    private val queue: PlayerQueue
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryState())
    val uiState: StateFlow<LibraryState> = _uiState.asStateFlow()

    fun fetchLibrary() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(data = LibraryDataState.Loading)
            }
            val library = client.get(Routes.LIBRARY).body<List<SongDto>>()
            updateDatabase(library)

            _uiState.update { state ->
                state.copy(data = LibraryDataState.Success(library))
            }
        }
    }

    fun playButtonClicked() {
        _uiState
            .value
            .data
            .takeIfInstance<LibraryDataState.Success>()
            ?.let { success ->
                val songs = success.items.shuffled()
                queue.set(
                    songs.map { song: SongDto ->
                        song.id
                    }
                )
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