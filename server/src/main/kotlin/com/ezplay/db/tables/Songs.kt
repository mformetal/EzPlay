package com.ezplay.db.tables

import com.ezplay.db.tables.Artists.autoIncrement
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object Songs : IntIdTable() {

    val name = varchar("songName", 128)
    val localPath = text("path")

    val albumId = reference("albumId", Albums.id)
}