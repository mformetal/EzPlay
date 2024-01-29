package metal.ezplay.nowplaying

import metal.ezplay.dto.SongDto

sealed class NowPlayingState {

    object Empty : NowPlayingState()

    data class Paused(val song: SongDto) : NowPlayingState()

    data class Playing(val song: SongDto) : NowPlayingState()
}