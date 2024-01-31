package metal.ezplay.nowplaying

import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import metal.ezplay.dto.SongDto
import metal.ezplay.network.EzPlayApi
import kotlin.math.max
import kotlin.math.min

class SongDownloader(private val ezPlayApi: EzPlayApi) {

    suspend fun download(songDto: SongDto) : Flow<HttpResponse> {
        val preview = ezPlayApi.preview(songDto)
        val chunkSize = max(DEFAULT_BUFFER_SIZE.toLong(), preview.fileSize.div(10))
        return flow {
            var downloaded = 0L

            while (downloaded < preview.fileSize) {
                val next = ezPlayApi.downloadChunk(
                    songDto,
                    downloaded,
                    downloaded + chunkSize
                )
                emit(next)

                downloaded += chunkSize
            }
        }
    }
}