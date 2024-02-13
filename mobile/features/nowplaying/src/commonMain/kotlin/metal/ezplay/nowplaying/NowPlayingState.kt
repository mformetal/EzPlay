package metal.ezplay.nowplaying

data class NowPlayingState(
    val songName: String = "",
    val artistName: String = "",
    val progress: Float = 0f,
    val isPlaying: Boolean = false
)
