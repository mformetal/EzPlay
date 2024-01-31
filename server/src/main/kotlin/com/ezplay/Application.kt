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
import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.network.tls.certificates.saveToFile
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
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
import io.ktor.utils.io.jvm.javaio.copyTo
import kotlinx.serialization.json.Json
import metal.ezplay.dto.AlbumDto
import metal.ezplay.dto.ArtistDto
import metal.ezplay.dto.PreviewDto
import metal.ezplay.dto.SongDto
import org.jaudiotagger.audio.AudioFileIO
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeBytes

fun main() {
    val keyStoreFile = File("server_keystore.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, "123456")

    val environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        connector {
            port = 8080
        }
        sslConnector(
            keyStore = keyStore,
            keyAlias = "sampleAlias",
            keyStorePassword = { "123456".toCharArray() },
            privateKeyPassword = { "foobar".toCharArray() }) {
            port = 8443
            keyStorePath = keyStoreFile
        }

        module {
            configureContentNegotiation()
            configureRouting()
            configureTemplating()
            configurePartialContent()

            DatabaseSingleton.init()
            DatabaseSingleton.populate(this, coroutineContext)
        }
    }

    embeddedServer(Netty, environment).start(wait = true)
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
                                imageUrl = "http://10.0.0.2:8080/library/artwork/${it.id.value}",
                                artist = ArtistDto(
                                    id = it.artist.id.value,
                                    name = it.artist.name
                                )
                            )
                        }
                }

                val file = File(DatabaseSingleton.query {
                    SongEntity.all().first()
                }.localPath)
                val audioTag = AudioFileIO.read(file)

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
                if (imageData == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                   call.respondBytes(imageData, ContentType.Image.JPEG, HttpStatusCode.OK)
                }
            }

            get("download/{id}") {
                val song = DatabaseSingleton.query {
                    SongEntity.find {
                        Songs.id eq call.parameters["id"]!!.toInt()
                    }.first()
                }

                val file = File(song.localPath)
                call.response.status(HttpStatusCode.OK)
                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.File.withParameter(ContentDisposition.Parameters.FileName, file.name)
                        .toString()
                )
                call.respondFile(file)
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
