package com.ezplay

import com.ezplay.db.tables.ArtistEntity
import com.ezplay.db.DatabaseSingleton
import com.ezplay.db.dto.ArtistDto
import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.*
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = {
        configureContentNegotiation()
        configureRouting()
        configureTemplating()

        DatabaseSingleton.init()
        DatabaseSingleton.populate(this, coroutineContext)
    }).start(wait = true)
}

private fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond(FreeMarkerContent("index.ftl", null))
        }

        route("library") {
            get {
                val library = DatabaseSingleton.query {
                    ArtistEntity.all().map(ArtistDto::fromEntity)
                }

                call.respond(library)
            }
        }
    }
}

private fun Application.configureTemplating() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        outputFormat = HTMLOutputFormat.INSTANCE
    }
}

private fun Application.configureContentNegotiation() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}
