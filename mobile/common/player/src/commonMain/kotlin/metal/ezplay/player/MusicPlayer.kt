package metal.ezplay.player

import kotlinx.coroutines.flow.Flow

expect class MusicPlayer {

    val isPlaying: Boolean

    suspend fun playerState(): Flow<MusicPlayerState>

    fun play()

    fun play(uri: String)

    fun pause()

    fun stop()
}