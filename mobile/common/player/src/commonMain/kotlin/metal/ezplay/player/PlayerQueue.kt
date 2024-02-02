package metal.ezplay.player

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import metal.ezplay.storage.AppDatabase

class PlayerQueue(
    private val scope: CoroutineScope,
    private val backgroundDispatcher: CoroutineDispatcher,
    private val player: MusicPlayer,
    private val downloader: SongDownloader,
    private val database: AppDatabase
) {

    private val queue = mutableSetOf<Int>()

    init {
        player.listener { state ->
            when (state) {
                MusicPlayerState.Finished -> {
                    playNext()
                }
                MusicPlayerState.Idle -> {
                    // who cares I guess
                }
                MusicPlayerState.Playing -> {
                    // get currently playing song from database?
                }
            }
        }
    }

    fun set(ids: List<Int>) {
        queue.clear()
        queue.addAll(ids)

        val head = queue.first()
        queue.remove(0)
        now(head)
    }

    fun enqueue(ids: List<Int>) {
        queue.addAll(ids)

        val head = queue.first()
        queue.remove(0)
        now(head)
    }

    fun now(songId: Int) {
        scope.launch {
            val path = withContext(backgroundDispatcher) {
                downloader.download(songId)
            }

            player.play(path.toString())
        }
    }

    private fun playNext() {
        if (queue.isNotEmpty()) {
            val head = queue.first()
            queue.remove(0)
            now(head)
        }
    }
}