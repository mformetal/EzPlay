package metal.ezplay.player

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.io.Sink
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import metal.ezplay.multiplatform.dto.PreviewDto
import metal.ezplay.network.Routes
import kotlin.math.max

class SongDownloader(private val client: HttpClient,
    private val fileSystem: FileSystem,
    private val internalFileStorage: Path) {

    suspend fun download(songId: Int): Path {
        val preview = client.get(Routes.preview(songId)).body<PreviewDto>()
        val audioFile = audioFilePath(preview.fileName)
        if (fileSystem.exists(audioFile)) return audioFile

        fileSystem.sink(audioFile).buffered()
            .use { sink ->
                downloadFile(sink, songId)
            }
        return audioFile
    }

    private fun audioFilePath(name: String): Path = Path(internalFileStorage, name)

    private suspend fun downloadFile(sink: Sink, songId: Int) {
        val preview = client.get(Routes.preview(songId)).body<PreviewDto>()
        val chunkSize = max(DEFAULT_BUFFER_SIZE.toLong(), preview.fileSize.div(10))
        var downloaded = 0L

        while (downloaded < preview.fileSize) {
            val chunk = client.get(Routes.download(songId)) {
                header("Range", "bytes=${downloaded}-${downloaded + chunkSize}")
            }
            writeTo(sink, chunk)

            downloaded += chunkSize
        }
    }

    private suspend fun writeTo(sink: Sink, response: HttpResponse) {
        val channel = response.bodyAsChannel()
        while (!channel.isClosedForRead) {
            val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
            while (!packet.isEmpty) {
                val bytes = packet.readBytes()
                sink.write(bytes)
            }
        }
    }
}