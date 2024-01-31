package metal.ezplay.entities

import kotlinx.serialization.Serializable

@Serializable
data class SongEntity(
    val id: Int,
    val albumId: Int,
    val artistId: Int,
    val name: String
)