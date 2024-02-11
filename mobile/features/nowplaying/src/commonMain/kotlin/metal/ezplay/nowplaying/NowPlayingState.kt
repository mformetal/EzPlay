package metal.ezplay.nowplaying

import androidx.compose.ui.graphics.drawscope.Stroke
import metal.ezplay.player.MusicPlayerState

data class NowPlayingState(
    val songName: String = "",
    val artistName: String = "",
    val progress: Float = 0f,
    val isPlaying: Boolean = false
)