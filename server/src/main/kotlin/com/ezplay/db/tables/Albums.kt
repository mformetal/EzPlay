package com.ezplay.db.tables

import com.ezplay.db.models.Artist
import com.ezplay.db.tables.Artists.autoIncrement
import org.jetbrains.exposed.sql.Table

object Albums : Table() {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)

    val artistId = reference("artistId", Artists.id)

    override val primaryKey = PrimaryKey(id)
}