package metal.ezplay.library

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import metal.ezplay.multiplatform.dto.PagedSongListRequest
import metal.ezplay.multiplatform.dto.PagedSongListResponse
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.multiplatform.extensions.takeIfInstance
import metal.ezplay.network.Routes
import metal.ezplay.player.PlayerQueue
import metal.ezplay.storage.AppDatabase
import metal.ezplay.viewmodel.MultiplatformViewModel

class LibraryViewModel(private val client: HttpClient,
    private val appDatabase: AppDatabase,
    private val queue: PlayerQueue
) : MultiplatformViewModel() {

    private var pagingState: LibraryPagingState ?= null

    private val _uiState = MutableStateFlow(LibraryState())
    val uiState: StateFlow<LibraryState> = _uiState.asStateFlow()

    fun fetchLibrary() {
        scope.launch {
            _uiState.update { state ->
                state.copy(data = LibraryDataState.Loading)
            }
            val library = client.post(Routes.LIBRARY) {
                setBody(PagedSongListRequest(pagingState?.current ?: 1))
            }.body<PagedSongListResponse>()

            if (pagingState == null) {
                pagingState = LibraryPagingState(
                    previous = library.previous,
                    next = library.next,
                    current = library.current,
                )
            }
            updateDatabase(library.songs)

            _uiState.update { state ->
                val existingItems = state.data.takeIfInstance<LibraryDataState.Success>()?.items ?: emptyList()
                state.copy(data = LibraryDataState.Success(library.songs + existingItems))
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