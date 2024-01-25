package com.ezplay.db.dto

import com.ezplay.db.tables.AlbumEntity
import com.ezplay.db.tables.SongEntity
import kotlinx.serialization.Serializable

@Serializable
class SongDto(
    val id: Int,
    val name: String
) {

    companion object {
        fun fromEntity(songEntity: SongEntity) =
            SongDto(
                id = songEntity.id.value,
                name = songEntity.name
            )
    }
}