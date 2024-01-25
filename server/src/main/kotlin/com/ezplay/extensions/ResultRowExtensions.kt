package com.ezplay.extensions

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.id(key: Column<EntityID<Int>>): Int = get(key).value