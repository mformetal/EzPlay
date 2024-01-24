package com.ezplay.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object Artists : IntIdTable() {

    val name = varchar("artistName", 128)

}