package metal.ezplay.search

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import metal.ezplay.multiplatform.dto.SongDto

class SearchResults(
    val searchTerm: String,
    val repositories: Flow<PagingData<SongDto>>
)
