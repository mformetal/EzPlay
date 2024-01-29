package metal.ezplay.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.ByteReadChannel
import metal.ezplay.dto.ArtistDto
import metal.ezplay.dto.SongDto

class EzPlayApi(private val client: HttpClient) {

    suspend fun library(): List<ArtistDto> =
        client.get(Routes.LIBRARY).body<List<ArtistDto>>()

    suspend fun play(songDto: SongDto): ByteReadChannel =
        client.get(Routes.play(songDto.id)).bodyAsChannel()
}