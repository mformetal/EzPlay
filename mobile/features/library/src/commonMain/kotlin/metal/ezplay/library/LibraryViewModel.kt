package metal.ezplay.library

import androidx.lifecycle.viewModelScope
import app.cash.paging.Pager
import app.cash.paging.PagingData
import app.cash.paging.map
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.multiplatform.dto.SongId
import metal.ezplay.network.Routes
import metal.ezplay.player.MusicPlayer
import metal.ezplay.player.PlayerQueue
import metal.ezplay.storage.AppDatabase
import metal.ezplay.viewmodel.MultiplatformViewModel

class LibraryViewModel(
    private val appDatabase: AppDatabase,
    private val client: HttpClient,
    private val queue: PlayerQueue,
    private val player: MusicPlayer,
    private val pager: Pager<Int, SongDto>
) : MultiplatformViewModel() {

    val songs: Flow<PagingData<SongDto>> = pager.flow

    init {
        scope.launch {
            pager.flow
                .map { pagingData ->
                    pagingData.map { song ->
                        updateDatabase(song)
                    }
                }
        }
    }

    fun shuffle() {
        player.stop()

        viewModelScope.launch {
            try {
                val response = client.get(Routes.ids())
                val ids = response.body<List<SongId>>()
                queue.shuffle(ids)
            } catch (e: Exception) {

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