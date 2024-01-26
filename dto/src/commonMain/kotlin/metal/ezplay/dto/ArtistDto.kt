package metal.ezplay.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    val id: Int,
    val name: String,
    val albums: List<AlbumDto>
) 