package metal.ezplay.nowplaying

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentDisposition
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import metal.ezplay.dto.SongDto
import metal.ezplay.network.EzPlayApi
import metal.ezplay.player.MusicPlayer
import metal.ezplay.storage.MusicFileStorage
import org.koin.core.component.getScopeId
import java.io.File

class NowPlayingViewModel(private val api: EzPlayApi,
                          private val downloader: SongDownloader,
                          private val musicPlayer: MusicPlayer,
    private val musicFileStorage: MusicFileStorage) : ViewModel() {

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

        viewModelScope.launch {
            val preview = api.preview(song)
            val audioFile = musicFileStorage.createFile(preview.fileName)
            downloader.download(song)
                .map {
                    musicFileStorage.downloadInto(it.bodyAsChannel(), audioFile)
                }
                .onStart {
                }
                .onCompletion {
                    musicPlayer.play(audioFile.toUri().toString())

                    _uiState.update {
                        it.copy(song = song, isPlaying = musicPlayer.isPlaying)
                    }
                }
                .collect()
        }
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