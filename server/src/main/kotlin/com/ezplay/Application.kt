package com.ezplay

import com.ezplay.db.DatabaseSingleton
import com.ezplay.db.tables.Albums
import com.ezplay.db.tables.Artists
import com.ezplay.db.tables.Songs
import com.ezplay.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = {
        configureRouting()
        DatabaseSingleton.init()
        DatabaseSingleton.populate(this, coroutineContext)
    }).start(wait = true)
}
