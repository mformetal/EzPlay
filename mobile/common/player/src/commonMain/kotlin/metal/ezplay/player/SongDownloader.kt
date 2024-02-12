package metal.ezplay.player

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.io.Sink
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import metal.ezplay.logging.SystemOut
import metal.ezplay.multiplatform.coroutines.retry
import metal.ezplay.multiplatform.dto.PreviewDto
import metal.ezplay.network.Routes
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import kotlin.math.max

class SongDownloader(
    private val scope: CoroutineScope,
    private val backgroundDispatcher: CoroutineDispatcher,
    private val client: HttpClient,
    private val fileSystem: FileSystem,
    private val internalFileStorage: Path) {

    suspend fun download(songId: Int): Path {
        val preview = client.get(Routes.Songs.preview(songId)).body<PreviewDto>()
        val audioFile = audioFilePath(preview.fileName)
        if (fileSystem.exists(audioFile)) return audioFile

        scope.launch(backgroundDispatcher) {
            downloadFile(audioFile, songId)
        }

        return audioFile
    }

    private fun audioFilePath(name: String): Path = Path(internalFileStorage, name)

    private suspend fun downloadFile(audioFilePath: Path, songId: Int) {
        val preview = client.get(Routes.Songs.preview(songId)).body<PreviewDto>()
        val chunkSize = max(DEFAULT_BUFFER_SIZE.toLong(), preview.fileSize.div(10))
        var downloaded = 0L
        val sink = fileSystem.sink(audioFilePath).buffered()
        var hasFileDownloadError = false

        while (downloaded < preview.fileSize && !hasFileDownloadError) {
            retry {
                client.get(Routes.Songs.downloadSong(songId)) {
                    header("Range", "bytes=${downloaded}-${downloaded + chunkSize}")
                }
            }.fold(onSuccess = { chunk ->
                writeTo(sink, chunk)

                downloaded += chunkSize
            }, onFailure = { downloadException ->
                SystemOut.exception(downloadException)

                hasFileDownloadError = true

                fileSystem.delete(audioFilePath)
            })
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

actual fun Scope.createSongDownloader(): SongDownloader {
    return SongDownloader(
        GlobalScope,
        Dispatchers.IO,
        get(),
        SystemFileSystem,
        Path(androidContext().filesDir.path)
    )
}