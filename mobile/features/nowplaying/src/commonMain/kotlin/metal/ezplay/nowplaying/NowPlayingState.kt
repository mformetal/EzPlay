package metal.ezplay.nowplaying

import metal.ezplay.dto.SongDto

data class NowPlayingState(
    val song: SongDto? = null,
    val isPlaying: Boolean = false
)