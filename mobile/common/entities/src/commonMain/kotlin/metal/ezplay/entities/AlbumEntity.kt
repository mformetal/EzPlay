package metal.ezplay.entities

import kotlinx.serialization.Serializable

@Serializable
data class AlbumEntity(
    val id: Int,
    val artistId: Int,
    val name: String,
    val songs: List<SongEntity>
)