package com.ezplay

import com.ezplay.db.DatabaseSingleton
import com.ezplay.db.dto.ArtistDto
import com.ezplay.db.tables.ArtistEntity
import com.ezplay.db.tables.SongEntity
import com.ezplay.db.tables.Songs
import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.partialcontent.PartialContent
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondBytesWriter
import io.ktor.server.response.respondFile
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.utils.io.jvm.javaio.copyTo
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.select
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = {
        configureContentNegotiation()
        configureRouting()
        configureTemplating()
        configurePartialContent()

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

            get("play/{id}") {
                val song = DatabaseSingleton.query {
                    SongEntity.find {
                        Songs.id eq call.parameters["id"]!!.toInt()
                    }.first()
                }

                val file = File(song.localPath)

                call.respondBytesWriter(ContentType.Audio.Any, HttpStatusCode.OK, file.length()) {
                    file.inputStream().buffered().copyTo(this)
                }
            }
        }
    }
}

private fun Application.configurePartialContent() {
    install(PartialContent)
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
