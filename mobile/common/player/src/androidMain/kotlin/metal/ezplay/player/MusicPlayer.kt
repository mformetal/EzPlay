package metal.ezplay.player

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Metadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import metal.ezplay.logging.SystemOut
import metal.ezplay.multiplatform.dto.SongDto
import metal.ezplay.storage.Song
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

actual class MusicPlayer(
    private val exoPlayer: ExoPlayer,
    private val scope: CoroutineScope,
    private val mainDispatcher: CoroutineDispatcher,
    private val backgroundDispatcher: CoroutineDispatcher) {

    private val listenerFlow = MutableSharedFlow<MusicPlayerState>(replay = 1)
    private var songDurationJob: Job?= null

    actual val playerState: Flow<MusicPlayerState> = listenerFlow

    actual var currentSong: SongDto? = null

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    songDurationJob?.cancel()
                    emitState(MusicPlayerState.Finished)
                } else if (exoPlayer.playWhenReady && playbackState == Player.STATE_READY) {
                    songDurationJob?.cancel()
                    songDurationJob = songPlayingJob()
                } else if (exoPlayer.playWhenReady) {
                    emitState(MusicPlayerState.Paused)
                }
            }

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                if (playWhenReady) {
                    songDurationJob?.cancel()
                    songDurationJob = songPlayingJob()
                } else {
                    songDurationJob?.cancel()
                    emitState(MusicPlayerState.Paused)
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                currentSong = mediaItem?.localConfiguration?.tag as? SongDto
            }
        })
    }

    actual val isPlaying: Boolean
        get() = exoPlayer.isPlaying

    actual fun play() {
        exoPlayer.play()
    }

    actual fun play(songDto: SongDto, uri: String) {
        with (exoPlayer) {
            val mediaItem = MediaItem.Builder()
                .setUri(uri)
                .setTag(songDto)
                .setMediaId(songDto.id.toString())
                .setMediaMetadata(MediaMetadata.Builder()
                    .setTitle(songDto.name)
                    .setAlbumTitle(songDto.album.name)
                    .setAlbumArtist(songDto.artist.name)
                    .setArtist(songDto.artist.name)
                    .build())
                .build()
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    actual fun restart() {
        exoPlayer.seekTo(0L)
        exoPlayer.playWhenReady = true
    }

    actual fun pause() {
        exoPlayer.pause()
    }

    actual fun stop() {
        exoPlayer.stop()
    }

    private fun emitState(state: MusicPlayerState) {
        scope.launch {
            listenerFlow.emit(state)
        }
    }

    private fun songPlayingJob(): Job {
        return scope.launch(mainDispatcher) {
            while (exoPlayer.currentPosition <= exoPlayer.duration) {
                emitState(MusicPlayerState.Playing(
                    songDto = requireNotNull(currentSong),
                    elapsed = exoPlayer.currentPosition,
                    total = exoPlayer.duration
                ))
                delay(100L)
            }
        }
    }
}

actual fun Scope.createMusicPlayer(): MusicPlayer {
    val extensionRendererMode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
    val renderersFactory = DefaultRenderersFactory(androidContext())
        .setExtensionRendererMode(extensionRendererMode)
        .setEnableDecoderFallback(true)
    val trackSelector = DefaultTrackSelector(androidContext())
    val exoPlayer = ExoPlayer.Builder(androidContext(), renderersFactory)
        .setTrackSelector(trackSelector)
        .build().apply {
            trackSelectionParameters = DefaultTrackSelector.Parameters.Builder(androidContext()).build()
            playWhenReady = false
        }

    return MusicPlayer(exoPlayer, CoroutineScope(Dispatchers.IO), Dispatchers.Main, Dispatchers.IO)
}