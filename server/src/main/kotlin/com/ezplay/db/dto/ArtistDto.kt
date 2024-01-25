package com.ezplay.db.dto

import com.ezplay.db.tables.ArtistEntity
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    val id: Int,
    val name: String,
    val albums: List<AlbumDto>
) {

    companion object {
        fun fromEntity(artistEntity: ArtistEntity) =
            ArtistDto(
                id = artistEntity.id.value,
                name = artistEntity.name,
                albums = artistEntity.albums.map(AlbumDto::fromEntity)
            )
    }
}