package metal.ezplay.search

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.viewmodel.MultiplatformViewModel

class SearchViewModel(private val client: HttpClient): MultiplatformViewModel() {

    var searchTerm = ""
        private set

    private val searchPager = Pager(PagingConfig(pageSize = 100, initialLoadSize = 100)) {
        SearchPagingSource(client, searchTerm)
    }
    val searchResults: Flow<PagingData<SongDto>> = searchPager.flow

    fun search(term: String) {
        searchTerm = term
    }
}