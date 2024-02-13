package metal.ezplay.nowplaying

import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import metal.ezplay.logging.SystemOut
import metal.ezplay.multiplatform.dto.SongId
import metal.ezplay.network.Routes
import metal.ezplay.player.MusicPlayer
import metal.ezplay.player.MusicPlayerState
import metal.ezplay.player.PlayerQueue
import metal.ezplay.viewmodel.MultiplatformViewModel

class NowPlayingViewModel(
    private val client: HttpClient,
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
            player.currentSong == null -> shuffle()
            player.isPlaying -> player.pause()
            else -> player.play()
        }
    }

    private fun shuffle() {
        player.stop()

        viewModelScope.launch {
            try {
                val response = client.get(Routes.Songs.ids())
                val ids = response.body<List<SongId>>()
                queue.shuffle(ids)
            } catch (e: IOException) {
                SystemOut.exception(e)
            }
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
