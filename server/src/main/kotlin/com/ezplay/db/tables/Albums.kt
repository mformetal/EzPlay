package com.ezplay.db.tables

import com.ezplay.db.models.Artist
import com.ezplay.db.tables.Artists.autoIncrement
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object Albums : IntIdTable() {

    val name = varchar("albumName", 128)

    val artistId = reference("artistId", Artists.id)
}