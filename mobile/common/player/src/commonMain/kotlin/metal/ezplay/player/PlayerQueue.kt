package metal.ezplay.player

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.multiplatform.dto.SongId
import metal.ezplay.network.Routes

class PlayerQueue(
    private val scope: CoroutineScope,
    private val mainDispatcher: CoroutineDispatcher,
    private val backgroundDispatcher: CoroutineDispatcher,
    private val client: HttpClient,
    private val player: MusicPlayer,
    private val downloader: SongDownloader
) {

    private var queueJob: Job ?= null
    private val queue = mutableListOf<SongId>()

    fun shuffle(ids: List<SongId>) {
        queueJob?.cancel()

        queue.clear()
        queue.addAll(ids)

        startQueueJob()
    }

    fun now(song: SongDto) {
        queueJob?.cancel()
        queue.clear()

        scope.launch(backgroundDispatcher) {
            downloadAndPlay(song)
        }
    }

    private fun startQueueJob() {
        queueJob = scope.launch(backgroundDispatcher) {
            playFromQueue()

            player.playerState
                .takeWhile { queue.isNotEmpty() }
                .filter {
                    it is MusicPlayerState.Finished
                }
                .onEach {
                    playFromQueue()
                }
                .collect()
        }
    }

    private suspend fun playFromQueue() {
        val next = queue.removeFirst()
        val song = client.get(Routes.song(next.id)).body<SongDto>()
        downloadAndPlay(song)
    }

    private suspend fun downloadAndPlay(song: SongDto) {
        val path = downloader.download(song.id)

        withContext(mainDispatcher) {
            player.play(song, path.toString())
        }
    }
}