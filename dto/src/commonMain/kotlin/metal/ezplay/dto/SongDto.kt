package metal.ezplay.dto

import kotlinx.serialization.Serializable

@Serializable
class SongDto(
    val id: Int,
    val name: String,
    val album: AlbumDto,
    val artist: ArtistDto
)