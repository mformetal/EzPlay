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

    fun now(songId: Int) {
        scope.launch(backgroundDispatcher) {
            val path = downloader.download(songId)

            withContext(mainDispatcher) {
                player.play(path.toString())
            }
        }
    }
}