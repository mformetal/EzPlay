package metal.ezplay.search

import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultError
import app.cash.paging.PagingSourceLoadResultPage
import app.cash.paging.PagingState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import metal.ezplay.multiplatform.dto.PagedSongListRequest
import metal.ezplay.multiplatform.dto.PagedSongListResponse
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.network.Routes

class SearchPagingSource(
  private val client: HttpClient,
  private val searchTerm: String
) : PagingSource<Int, SongDto>() {

  override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, SongDto> {
    val page = params.key ?: 1
    val httpResponse = client.post(Routes.Songs.page()) {
      setBody(PagedSongListRequest(page = page, searchTerm = searchTerm))
    }

    return when {
      httpResponse.status.isSuccess() -> {
        val response = httpResponse.body<PagedSongListResponse>()
        PagingSourceLoadResultPage(
          data = response.songs,
          prevKey = response.previous,
          nextKey = response.next,
        )
      }
      httpResponse.status == HttpStatusCode.Forbidden -> {
        PagingSourceLoadResultError(
          Exception("Whoops! You just exceeded the GitHub API rate limit."),
        )
      }
      else -> {
        PagingSourceLoadResultError(
          Exception("Received a ${httpResponse.status}."),
        )
      }
    }
  }

  override fun getRefreshKey(state: PagingState<Int, SongDto>): Int? = null
}