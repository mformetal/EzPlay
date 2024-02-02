package metal.ezplay.player

expect class MusicPlayer {

    val isPlaying: Boolean

    fun listener(onPlayerStateChanged: (MusicPlayerState) -> Unit)

    fun play()

    fun play(uri: String)

    fun pause()

    fun stop()
}