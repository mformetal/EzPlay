package metal.ezplay.nowplaying

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
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
import kotlinx.coroutines.launch
import metal.ezplay.dto.SongDto
import metal.ezplay.network.EzPlayApi
import metal.ezplay.storage.MusicFileStorage
import java.io.File

class NowPlayingViewModel(private val api: EzPlayApi,
    private val exoPlayer: ExoPlayer,
                          private val downloader: SongDownloader,
    private val musicFileStorage: MusicFileStorage) : ViewModel() {

    private val _uiState = MutableStateFlow(NowPlayingState.Empty)
    val uiState: StateFlow<NowPlayingState> = _uiState.asStateFlow()

    fun play(song: SongDto) {
        viewModelScope.launch {
            val preview = api.preview(song)
            val audioFile = musicFileStorage.createFile(preview.fileName)
            downloader.download(song)
                .map {
                    musicFileStorage.downloadInto(it.bodyAsChannel(), audioFile)
                }
                .onStart {
                    println("START DOWNLOADING")
                }
                .onCompletion {
                    println("DONE DOWNLOADING")
                    val mediaItem = MediaItem.fromUri(audioFile.toUri())
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true
                }
                .collect()
        }
    }
}