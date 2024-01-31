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
            appDatabase.artistQueries.transaction {
                library.forEach { artistDto ->
                    appDatabase.artistQueries.insert(
                        artistDto.id.toLong(),
                        artistDto.name
                    )
                }
            }

            appDatabase.albumQueries.transaction {
                library.associateBy { artistDto ->
                    artistDto.albums
                }.forEach { (albums, artist) ->
                    albums.forEach { album ->
                        appDatabase.albumQueries
                            .insert(
                                album.id.toLong(),
                                artist.id.toLong(),
                                album.name
                            )
                    }
                }
            }

            appDatabase.songQueries.transaction {
                library.forEach { artist ->
                    artist.albums.forEach { album ->
                        album.songs.forEach { song ->
                            appDatabase.songQueries
                                .insert(
                                    song.id.toLong(),
                                    album.id.toLong(),
                                    artist.id.toLong(),
                                    song.name
                                )
                        }
                    }
                }
            }
        }
    }
}