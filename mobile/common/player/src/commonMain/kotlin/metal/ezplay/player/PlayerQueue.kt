package metal.ezplay.player

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import metal.ezplay.multiplatform.dto.SongDto

class PlayerQueue(
    private val scope: CoroutineScope,
    private val mainDispatcher: CoroutineDispatcher,
    private val backgroundDispatcher: CoroutineDispatcher,
    private val player: MusicPlayer,
    private val downloader: SongDownloader
) {

    fun now(song: SongDto) {
        scope.launch(backgroundDispatcher) {
            val path = downloader.download(song.id)

            withContext(mainDispatcher) {
                player.play(song, path.toString())
            }
        }
    }
}