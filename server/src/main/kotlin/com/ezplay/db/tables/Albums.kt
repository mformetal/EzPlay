package com.ezplay.db.tables

import metal.ezplay.dto.AlbumDto
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Albums : IntIdTable() {

    val name = varchar("albumName", 128)

    val artist = reference("artist", Artists)
}

class AlbumEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<AlbumEntity>(Albums)

    val name by Albums.name
    val artistEntity by ArtistEntity referencedOn Albums.artist
    val songs by SongEntity referrersOn Songs.album

    fun toDto() =
        AlbumDto(
            id = id.value,
            name = name,
            songs = songs.map(SongEntity::toDto)
        )
}