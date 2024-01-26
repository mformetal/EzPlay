package metal.ezplay.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import metal.ezplay.dto.ArtistDto

class EzPlayApi(private val client: HttpClient) {

    suspend fun library(): List<ArtistDto> =
        client.get(Routes.LIBRARY).body<List<ArtistDto>>()
}