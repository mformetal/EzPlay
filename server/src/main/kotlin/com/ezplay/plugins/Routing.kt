package com.ezplay.plugins

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

fun Application.configureRouting() {
    routing {
        static("/static") {
            resources("files")
        }

        get("/") {
            call.respondRedirect("music")
        }

        route("music") {
            get {
                call.respond(FreeMarkerContent("index.ftl", null))
            }
        }
    }
}
