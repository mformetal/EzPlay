package metal.ezplay.multiplatform.dto

import kotlinx.serialization.Serializable

@Serializable
class PagedSongListResponse(
    val previous: Int?,
    val next: Int?,
    val current: Int,
    val songs: List<SongDto>
)