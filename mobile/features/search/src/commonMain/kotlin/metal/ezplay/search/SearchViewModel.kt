package metal.ezplay.search

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.player.PlayerQueue
import metal.ezplay.viewmodel.MultiplatformViewModel

class SearchViewModel(
    private val client: HttpClient,
    private val queue: PlayerQueue
) : MultiplatformViewModel() {

    var searchTerm = ""
        private set

    private val searchPager = Pager(PagingConfig(pageSize = 100, initialLoadSize = 100)) {
        SearchPagingSource(client, searchTerm)
    }
    val searchResults: Flow<PagingData<SongDto>> = searchPager.flow

    fun search(term: String) {
        searchTerm = term
    }

    fun play(song: SongDto) {
        queue.now(song)
    }
}
