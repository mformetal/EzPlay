package metal.ezplay.player

import kotlinx.coroutines.flow.Flow
import metal.ezplay.multiplatform.dto.SongDto

expect class MusicPlayer {

    val isPlaying: Boolean

    var currentSong: SongDto?

    val playerState: Flow<MusicPlayerState>

    fun play()

    fun play(songDto: SongDto, uri: String)

    fun pause()

    fun stop()
}