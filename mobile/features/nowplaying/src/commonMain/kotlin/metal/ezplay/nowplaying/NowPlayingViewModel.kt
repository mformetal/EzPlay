package metal.ezplay.nowplaying

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import metal.ezplay.player.MusicPlayer
import metal.ezplay.player.MusicPlayerState
import metal.ezplay.player.PlayerQueue
import metal.ezplay.viewmodel.MultiplatformViewModel

class NowPlayingViewModel(
    private val player: MusicPlayer,
    private val queue: PlayerQueue
) : MultiplatformViewModel() {

    private val _uiState = MutableStateFlow(NowPlayingState())
    val uiState: StateFlow<NowPlayingState> = _uiState.asStateFlow()

    init {
        listenToPlayerState()
    }

    fun musicControlsClicked() {
        when {
            player.currentSong == null -> queue.shuffle()
            player.isPlaying -> player.pause()
            else -> player.play()
        }
    }

    private fun listenToPlayerState() {
        viewModelScope.launch {
            player.playerState
                .collect { playerState ->
                    when (playerState) {
                        MusicPlayerState.Finished -> { }
                        MusicPlayerState.Loading -> { }
                        MusicPlayerState.Paused -> {
                            _uiState.update {
                                it.copy(isPlaying = false)
                            }
                        }
                        is MusicPlayerState.Playing -> {
                            _uiState.update {
                                it.copy(
                                    songName = playerState.songDto.name,
                                    artistName = playerState.songDto.artist.name,
                                    progress = it.progress,
                                    isPlaying = true
                                )
                            }
                        }
                    }
                }
        }
    }
}
