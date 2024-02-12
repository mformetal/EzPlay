package metal.ezplay.player

import metal.ezplay.multiplatform.dto.SongDto

sealed class MusicPlayerState {

    data class Playing(val songDto: SongDto, val elapsed: Long, val total: Long) : MusicPlayerState()

    object Paused : MusicPlayerState()

    object Loading : MusicPlayerState()

    object Finished : MusicPlayerState()
}