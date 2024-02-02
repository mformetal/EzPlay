package metal.ezplay.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import metal.ezplay.dto.SongDto
import metal.ezplay.player.MusicPlayer
import metal.ezplay.player.MusicPlayerState
import metal.ezplay.player.PlayerQueue
import metal.ezplay.storage.AppDatabase

class NowPlayingViewModel(
    private val musicPlayer: MusicPlayer,
    private val queue: PlayerQueue
) : ViewModel() {

    private val _uiState = MutableStateFlow(NowPlayingState())
    val uiState: StateFlow<NowPlayingState> = _uiState.asStateFlow()

    fun musicControlsClicked() {
        if (musicPlayer.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun play(song: SongDto) {
        musicPlayer.stop()

        queue.now(song.id)
    }

    private fun pause() {
        musicPlayer.pause()

        _uiState.update {
            it.copy(isPlaying = musicPlayer.isPlaying)
        }
    }

    private fun play() {
        musicPlayer.play()

        _uiState.update {
            it.copy(isPlaying = musicPlayer.isPlaying)
        }
    }
}