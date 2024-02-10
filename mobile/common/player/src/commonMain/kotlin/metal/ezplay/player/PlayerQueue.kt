package metal.ezplay.player

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerQueue(
    private val scope: CoroutineScope,
    private val mainDispatcher: CoroutineDispatcher,
    private val backgroundDispatcher: CoroutineDispatcher,
    private val player: MusicPlayer,
    private val downloader: SongDownloader
) {

    private val queue = mutableSetOf<Int>()

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

            withContext(mainDispatcher) {
                player.play(path.toString())
            }
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