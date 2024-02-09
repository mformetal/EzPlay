package com.ezplay

import com.ezplay.db.DatabaseSingleton
import com.ezplay.db.tables.SongEntity
import com.ezplay.db.tables.Songs
import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.serialization.suitableCharset
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.partialcontent.PartialContent
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondBytesWriter
import io.ktor.server.response.respondFile
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.converter
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.jvm.javaio.copyTo
import io.ktor.websocket.serialization.sendSerializedBase
import kotlinx.serialization.json.Json
import metal.ezplay.multiplatform.dto.AlbumDto
import metal.ezplay.multiplatform.dto.ArtistDto
import metal.ezplay.multiplatform.dto.PreviewDto
import metal.ezplay.multiplatform.dto.SongDto
import org.jaudiotagger.audio.AudioFileIO
import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeBytes

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = {
        configureWebSockets()
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
                val songs = DatabaseSingleton.query {
                    SongEntity.all()
                        .map {
                            SongDto(
                                id = it.id.value,
                                name = it.name,
                                album = AlbumDto(id = it.album.id.value,
                                    name = it.album.name),
                                imageUrl = "",
                                artist = ArtistDto(
                                    id = it.artist.id.value,
                                    name = it.artist.name
                                )
                            )
                        }
                }

                call.respond(songs)
            }

            get("preview/{id}") {
                val song = DatabaseSingleton.query {
                    SongEntity.find {
                        Songs.id eq call.parameters["id"]!!.toInt()
                    }.first()
                }

                val file = File(song.localPath)
                call.respond(PreviewDto(
                    fileSize = file.length(),
                    fileName = file.name
                ))
            }

            get("artwork/{id}") {
                val song = DatabaseSingleton.query {
                    SongEntity.find {
                        Songs.id eq call.parameters["id"]!!.toInt()
                    }.first()
                }

                val file = File(song.localPath)
                val audioFile = AudioFileIO.read(file)
                val artwork = audioFile.tag.firstArtwork
                val imageData = artwork?.binaryData
//                if (imageData == null) {
//                    call.respond(HttpStatusCode.NotFound)
//                } else {
//                   call.respondBytes(imageData, ContentType.Image.JPEG, HttpStatusCode.OK)
//                }
            }

            get("download/{id}") {
                val song = DatabaseSingleton.query {
                    SongEntity.find {
                        Songs.id eq call.parameters["id"]!!.toInt()
                    }.first()
                }


            }

            webSocket("download_ws/{id}") {
                val song = DatabaseSingleton.query {
                    SongEntity.find {
                        Songs.id eq call.parameters["id"]!!.toInt()
                    }.first()
                }
                val file = File(song.localPath)

                val previewDto = PreviewDto(
                    fileSize = file.length(),
                    fileName = file.name
                )
            }

            get("play/{id}") {
                val song = DatabaseSingleton.query {
                    SongEntity.find {
                        Songs.id eq call.parameters["id"]!!.toInt()
                    }.first()
                }

                val file = File(song.localPath)
                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.File.withParameter(ContentDisposition.Parameters.FileName, file.name)
                        .toString()
                )
                call.response.header(
                    HttpHeaders.ContentLength,
                    file.length()
                )
                call.response.status(HttpStatusCode.OK)
                call.respondFile(file)
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

private fun Application.configureWebSockets() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}
