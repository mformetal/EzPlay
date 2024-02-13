package metal.ezplay.multiplatform.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    val id: Int,
    val name: String
)
