package metal.ezplay.player

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.consumeEachBufferRange
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readByteBuffer
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.io.Buffer
import kotlinx.io.RawSink
import kotlinx.io.Sink
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.files.sink
import metal.ezplay.network.EzPlayApi
import java.io.File
import java.nio.ByteBuffer
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class SongDownloader(private val ezPlayApi: EzPlayApi,
    private val fileSystem: FileSystem,
    private val internalFileStorage: Path) {

    suspend fun download(songId: Int): Path {
        val preview = ezPlayApi.preview(songId)
        val audioFile = audioFilePath(preview.fileName)
        if (fileSystem.exists(audioFile)) return audioFile

        fileSystem.sink(audioFile).buffered()
            .use { sink ->
                downloadChunks(songId)
                    .map {
                        downloadInto(it.bodyAsChannel(), sink)
                    }
                    .collect()
            }
        return audioFile
    }

    private fun audioFilePath(name: String): Path = Path(internalFileStorage, name)

    private suspend fun downloadChunks(songId: Int) : Flow<HttpResponse> {
        val preview = ezPlayApi.preview(songId)
        val chunkSize = max(DEFAULT_BUFFER_SIZE.toLong(), preview.fileSize.div(10))
        return flow {
            var downloaded = 0L

            while (downloaded < preview.fileSize) {
                val chunk = ezPlayApi.downloadChunk(
                    songId,
                    downloaded,
                    downloaded + chunkSize
                )
                emit(chunk)

                downloaded += chunkSize
            }
        }
    }

    private suspend fun doDownload(songId: Int, downloaded: Long, chunkSize: Long): HttpResponse =
        ezPlayApi.downloadChunk(
            songId,
            downloaded,
            downloaded + chunkSize
        )

    private suspend fun downloadInto(channel: ByteReadChannel, sink: Sink) {
        while (!channel.isClosedForRead) {
            val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
            while (!packet.isEmpty) {
                val bytes = packet.readBytes()
                sink.write(bytes)
            }
        }
    }
}