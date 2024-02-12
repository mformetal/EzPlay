package metal.ezplay.player

import androidx.compose.runtime.MutableState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.multiplatform.dto.SongId
import metal.ezplay.network.Routes
import kotlin.math.max
import kotlin.math.min

class PlayerQueue(
    private val scope: CoroutineScope,
    private val mainDispatcher: CoroutineDispatcher,
    private val backgroundDispatcher: CoroutineDispatcher,
    private val client: HttpClient,
    private val player: MusicPlayer,
    private val downloader: SongDownloader
) {

    private var queueJob: Job ?= null
    private val indexFlow = MutableStateFlow(-1)
    private val queue = mutableListOf<SongId>()

    fun shuffle(ids: List<SongId>) {
        queueJob?.cancel()

        queue.clear()
        queue.addAll(ids)

        indexFlow.update { 0 }

        startQueueJob()
    }

    fun next() {
        indexFlow.update { min(indexFlow.value.inc(), queue.lastIndex) }
    }

    fun previous() {
        indexFlow.update { max(indexFlow.value.dec(), 0) }
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
            playFromQueue(indexFlow.value)

            launch {
                indexFlow
                    .onEach {
                        playFromQueue(it)
                    }.collect()
            }

            launch {
                player.playerState
                    .filter { it is MusicPlayerState.Finished }
                    .onEach {
                        next()
                        playFromQueue(indexFlow.value)
                    }
                    .collect()
            }
        }
    }

    private suspend fun playFromQueue(index: Int) {
        val next = queue[index]
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