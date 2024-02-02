package metal.ezplay.player

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import metal.ezplay.storage.AppDatabase

class PlayerQueue(
    private val scope: CoroutineScope,
    private val player: MusicPlayer,
    private val downloader: SongDownloader) {

    private val queue = mutableListOf<Int>()

    init {
        player.listener { state ->
            when (state) {
                MusicPlayerState.Finished -> {
                    // if there are more items in the queue, download the next one and play it
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

    fun enqueue(vararg songId: Int) {

    }

    fun now(songId: Int) {
        scope.launch {
            val path = downloader.download(songId)
            player.play(path.toString())
        }
    }

    fun remove(songId: Int) {

    }
}