package com.ezplay.db.tables

import metal.ezplay.dto.ArtistDto
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Artists : IntIdTable() {

    val name = varchar("artistName", 128)
}

class ArtistEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<ArtistEntity>(Artists)

    val name by Artists.name
    val albums by AlbumEntity referrersOn Albums.artist
}