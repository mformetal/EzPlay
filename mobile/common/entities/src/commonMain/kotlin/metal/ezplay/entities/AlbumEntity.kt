package metal.ezplay.entities

import kotlinx.serialization.Serializable

@Serializable
data class AlbumEntity(
    val id: Int,
    val name: String,
    val artist: ArtistEntity,
    val songs: List<SongEntity>
)