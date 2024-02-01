package metal.ezplay.nowplaying

import metal.ezplay.dto.SongDto

interface MusicPlayer {

    fun play(songDto: SongDto)

    fun pause()
}