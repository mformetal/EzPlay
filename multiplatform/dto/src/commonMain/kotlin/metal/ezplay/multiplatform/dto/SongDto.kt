package metal.ezplay.multiplatform.dto

import kotlinx.serialization.Serializable

@Serializable
class SongDto(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val album: AlbumDto,
    val artist: ArtistDto
)