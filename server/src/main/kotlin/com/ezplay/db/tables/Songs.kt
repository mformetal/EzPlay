package com.ezplay.db.tables

import kotlinx.io.files.Path
import metal.ezplay.multiplatform.dto.AlbumDto
import metal.ezplay.multiplatform.dto.ArtistDto
import metal.ezplay.multiplatform.dto.SongDto
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Songs : IntIdTable() {

    val name = varchar("songName", 128)
    val localPath = text("path")
    val duration = integer("duration")

    val album = reference("album", Albums)
    val artist = reference("artist", Artists)
}

class SongEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<SongEntity>(Songs)

    val name by Songs.name
    val localPath by Songs.localPath
    val duration by Songs.duration
    val album by AlbumEntity referencedOn Songs.album
    val artist by ArtistEntity referencedOn Songs.artist

    val filePath: Path
        get() = Path(localPath)

    fun toDto() = SongDto(
        id = id.value,
        name = name,
        album = AlbumDto(id = album.id.value,
            name = album.name),
        imageUrl = "",
        artist = ArtistDto(
            id = artist.id.value,
            name = artist.name
        )
    )
}