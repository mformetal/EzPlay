package metal.ezplay.nowplaying

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSourceInputStream
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentDisposition
import io.ktor.http.HttpHeaders
import io.ktor.http.contentRangeHeaderValue
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import metal.ezplay.dto.SongDto
import metal.ezplay.network.EzPlayApi
import metal.ezplay.storage.MusicFileStorage
import java.io.File

class NowPlayingViewModel(private val api: EzPlayApi,
    private val exoPlayer: ExoPlayer,
    private val musicFileStorage: MusicFileStorage) : ViewModel() {

    private val _uiState = MutableStateFlow(NowPlayingState.Empty)
    val uiState: StateFlow<NowPlayingState> = _uiState.asStateFlow()

    fun play(song: SongDto) {
        viewModelScope.launch {
            val response = api.play(song)
            val audioFile = saveFileToStorage(response)
            val mediaItem = MediaItem.fromUri(audioFile.toUri())
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    private suspend fun saveFileToStorage(response: HttpResponse): File {
        val contentDisposition = ContentDisposition.parse(response.headers[HttpHeaders.ContentDisposition]!!)
        val fileName = contentDisposition.parameter(ContentDisposition.Parameters.FileName)!!
        return musicFileStorage.download(response.bodyAsChannel(), fileName)
    }
}