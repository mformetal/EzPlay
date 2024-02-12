package metal.ezplay.player

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.multiplatform.dto.SongId
import metal.ezplay.multiplatform.extensions.takeIfInstance
import metal.ezplay.network.Routes
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.seconds

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
        val goBack = {
            indexFlow.update { max(indexFlow.value.dec(), 0) }
        }
        scope.launch(backgroundDispatcher) {
            try {
                val currentState = player.playerState
                    .timeout(100.microseconds)
                    .first()
                    .takeIfInstance<MusicPlayerState.Playing>()
                    ?.run {
                        elapsed.toFloat().div(total.toFloat())
                    }

                when {
                    currentState == null -> goBack()
                    currentState < .05 -> {
                        withContext(mainDispatcher) {
                            player.restart()
                        }
                    }
                    else -> goBack()
                }
            } catch (e: NoSuchElementException) {
                goBack()
            } catch (e: TimeoutCancellationException) {
                goBack()
            }
        }
    }

    fun now(song: SongDto) {
        queueJob?.cancel()
        queue.clear()

        player.stop()

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
        val song = client.get(Routes.Songs.info(next.id)).body<SongDto>()
        downloadAndPlay(song)
    }

    private suspend fun downloadAndPlay(song: SongDto) {
        val path = downloader.download(song.id)

        withContext(mainDispatcher) {
            player.play(song, path.toString())
        }
    }
}