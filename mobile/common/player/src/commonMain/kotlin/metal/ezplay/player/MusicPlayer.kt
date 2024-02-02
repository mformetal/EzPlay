package metal.ezplay.player

expect class MusicPlayer {

    val isPlaying: Boolean

    fun play()

    fun play(uri: String)

    fun pause()

    fun stop()
}