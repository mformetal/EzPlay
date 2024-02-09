package metal.ezplay.library

import androidx.lifecycle.viewModelScope
import androidx.paging.flatMap
import app.cash.paging.LoadState
import app.cash.paging.LoadStates
import app.cash.paging.Pager
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.flatMap
import app.cash.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.multiplatform.extensions.takeIfInstance
import metal.ezplay.player.PlayerQueue
import metal.ezplay.storage.AppDatabase
import metal.ezplay.viewmodel.MultiplatformViewModel

class LibraryViewModel(
    private val appDatabase: AppDatabase,
    private val queue: PlayerQueue,
    private val pager: Pager<Int, SongDto>
) : MultiplatformViewModel() {

    val songs: Flow<PagingData<SongDto>> = pager.flow

    init {
        viewModelScope.launch {
            pager.flow
                .map {
                    it.map {

                    }
                }
        }
    }

    fun playButtonClicked() {
//        _uiState
//            .value
//            .data
//            .takeIfInstance<LibraryDataState.Success>()
//            ?.let { success ->
//                val songs = success.items.shuffled()
//                queue.set(
//                    songs.map { song: SongDto ->
//                        song.id
//                    }
//                )
//            }
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