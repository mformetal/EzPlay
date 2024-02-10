package metal.ezplay.library

import androidx.lifecycle.viewModelScope
import app.cash.paging.Pager
import app.cash.paging.PagingData
import app.cash.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.storage.AppDatabase
import metal.ezplay.viewmodel.MultiplatformViewModel

class LibraryViewModel(
    private val appDatabase: AppDatabase,
    private val pager: Pager<Int, SongDto>
) : MultiplatformViewModel() {

    val songs: Flow<PagingData<SongDto>> = pager.flow

    init {
        viewModelScope.launch {
            pager.flow
                .map { pagingData ->
                    pagingData.map { song ->
                        updateDatabase(song)
                    }
                }
        }
    }

    private suspend fun updateDatabase(song: SongDto) {
        withContext(Dispatchers.IO) {
            appDatabase.artistQueries.transaction {
                appDatabase.artistQueries.insert(
                    song.id.toLong(),
                    song.name
                )
            }

            appDatabase.albumQueries.transaction {
                appDatabase.albumQueries.insert(
                    song.album.id.toLong(),
                    song.artist.id.toLong(),
                    song.album.name
                )
            }

            appDatabase.songQueries.transaction {
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