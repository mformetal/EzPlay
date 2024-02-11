package metal.ezplay.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import metal.ezplay.logging.SystemOut
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.player.MusicPlayer
import metal.ezplay.player.MusicPlayerState
import metal.ezplay.player.PlayerQueue
import metal.ezplay.storage.AppDatabase
import metal.ezplay.viewmodel.MultiplatformViewModel

class NowPlayingViewModel(
    private val musicPlayer: MusicPlayer,
    private val queue: PlayerQueue
) : MultiplatformViewModel() {

    private val _uiState = MutableStateFlow(NowPlayingState())
    val uiState: StateFlow<NowPlayingState> = _uiState.asStateFlow()

    init {
        listenToPlayerState()
    }

    fun musicControlsClicked() {
        if (musicPlayer.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun play(song: SongDto) {
        musicPlayer.stop()

        queue.now(song)
    }

    private fun pause() {
        musicPlayer.pause()
    }

    private fun play() {
        musicPlayer.play()
    }

    private fun listenToPlayerState() {
        viewModelScope.launch {
            musicPlayer.playerState
                .collect { playerState ->
                    when (playerState) {
                        MusicPlayerState.Idle -> { }
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
                                    progress = playerState.elapsed.toFloat().div(playerState.total.toFloat()),
                                    isPlaying = true
                                )
                            }
                        }
                    }
                }
        }
    }
}