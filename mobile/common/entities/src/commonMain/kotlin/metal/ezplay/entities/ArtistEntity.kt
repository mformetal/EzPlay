package metal.ezplay.entities

import kotlinx.serialization.Serializable

@Serializable
data class ArtistEntity(
    val id: Int,
    val name: String,
    val albums: List<AlbumEntity>
)