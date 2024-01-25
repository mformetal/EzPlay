package com.ezplay.db.dto

import com.ezplay.db.tables.AlbumEntity
import com.ezplay.db.tables.ArtistEntity
import com.ezplay.db.tables.SongEntity
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    val id: Int,
    val name: String,
    val songs: List<SongDto>
) {

    companion object {
        fun fromEntity(albumEntity: AlbumEntity) =
            AlbumDto(
                id = albumEntity.id.value,
                name = albumEntity.name,
                songs = albumEntity.songs.map(SongDto::fromEntity)
            )
    }
}
