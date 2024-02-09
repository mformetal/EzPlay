package metal.ezplay.multiplatform.dto

import kotlinx.serialization.Serializable

@Serializable
class PagedSongListRequest(
    val page: Int
)