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
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.plugins.partialcontent.PartialContent
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondBytesWriter
import io.ktor.server.response.respondFile
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.utils.io.jvm.javaio.copyTo
import kotlinx.serialization.json.Json
import metal.ezplay.multiplatform.dto.AlbumDto
import metal.ezplay.multiplatform.dto.ArtistDto
import metal.ezplay.multiplatform.dto.PagedSongListRequest
import metal.ezplay.multiplatform.dto.PagedSongListResponse
import metal.ezplay.multiplatform.dto.PreviewDto
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.multiplatform.dto.SongId
import org.jaudiotagger.audio.AudioFileIO
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeBytes

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = {
        configureContentNegotiation()
        configureRouting()
        configureTemplating()
        configurePartialContent()
        install(DoubleReceive)

        DatabaseSingleton.init()
        DatabaseSingleton.populate(this, coroutineContext)
    }).start(wait = true)
}

private fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond(FreeMarkerContent("index.ftl", null))
        }

        route("songs") {
            post {
                val pageSize = 100L
                val request = call.receive<PagedSongListRequest>()
                val pageNumber = request.page
                val offset = (pageNumber - 1) * pageSize
                val numSongs = DatabaseSingleton.query {
                    SongEntity.count()
                }
                val songs = DatabaseSingleton.query {
                    SongEntity.all()
                        .orderBy(Songs.name to SortOrder.DESC)
                        .limit(n = pageSize.toInt(), offset = offset)
                        .map(SongEntity::toDto)
                }

                val lastPage = numSongs.div(pageSize)

                val previousNumber = if (pageNumber == 1) {
                    null
                } else {
                    pageNumber.dec()
                }

                val nextPageNumber = if (pageNumber.toLong() == lastPage) {
                    null
                } else {
                    pageNumber.inc()
                }

                call.respond(PagedSongListResponse(
                    previous = previousNumber,
                    next = nextPageNumber,
                    songs = songs,
                    current = pageNumber
                ))
            }

            get("ids") {
                val songs = DatabaseSingleton.query {
                    Songs.slice(Songs.id)
                        .selectAll()
                        .map {
                            SongId(id = it[Songs.id].value)
                        }
                }

                call.respond(songs.shuffled())
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

            get("{id}") {
                val song = DatabaseSingleton.query {
                    SongEntity.find {
                        Songs.id eq call.parameters["id"]!!.toInt()
                    }.first().toDto()
                }

                call.respond(song)
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
