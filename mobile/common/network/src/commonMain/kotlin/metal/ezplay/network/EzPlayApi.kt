package metal.ezplay.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.ByteReadChannel
import metal.ezplay.dto.ArtistDto
import metal.ezplay.dto.PreviewDto
import metal.ezplay.dto.SongDto

class EzPlayApi(private val client: HttpClient) {

    suspend fun preview(songDto: SongDto): PreviewDto =
        client.get(Routes.preview(songDto.id)).body()

    suspend fun downloadChunk(songDto: SongDto, start: Long, end: Long): HttpResponse
        = client.get(Routes.download(songDto.id)) {
            header("Range", "bytes=${start}-${end}")
        }

    suspend fun library(): List<ArtistDto> =
        client.get(Routes.LIBRARY).body<List<ArtistDto>>()

    suspend fun play(songDto: SongDto): HttpResponse =
        client.get(Routes.play(songDto.id))
}