package com.ezplay.plugins

import com.ezplay.db.DatabaseSingleton
import com.ezplay.db.models.Album
import com.ezplay.db.models.Artist
import com.ezplay.db.models.Song
import com.ezplay.db.tables.Albums
import com.ezplay.db.tables.Artists
import com.ezplay.db.tables.Songs
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.Join
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

fun Application.configureRouting() {
    routing {
        route("music") {
            get {
                val library = DatabaseSingleton.query {
                   Artists.innerJoin(Albums).innerJoin(Songs)
                       .selectAll()
                       .forEach {
                           println("ROW: ${it.get(Artists.name)}, ${it.get(Albums.name)}, ${it.get(Songs.name)}")
                       }
                }

                call.respond(FreeMarkerContent("index.ftl", null))
            }
        }
    }
}
