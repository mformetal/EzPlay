package metal.ezplay.multiplatform.dto

import kotlinx.serialization.Serializable

@Serializable
data class SongDto(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val album: AlbumDto,
    val artist: ArtistDto
)