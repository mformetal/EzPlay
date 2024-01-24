package com.ezplay.db.tables

import org.jetbrains.exposed.sql.Table

object Artists : Table() {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)

    override val primaryKey = PrimaryKey(id)
}