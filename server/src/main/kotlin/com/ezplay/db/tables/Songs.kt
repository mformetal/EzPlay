package com.ezplay.db.tables

import com.ezplay.db.tables.Artists.autoIncrement
import org.jetbrains.exposed.sql.Table

object Songs : Table() {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)

    val albumId = reference("albumId", Albums.id)
    val artistId = reference("artistId", Artists.id)

    override val primaryKey = PrimaryKey(id)
}