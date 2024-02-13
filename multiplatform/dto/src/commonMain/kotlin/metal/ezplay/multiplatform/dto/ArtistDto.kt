package metal.ezplay.multiplatform.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    val id: Int,
    val name: String
)
