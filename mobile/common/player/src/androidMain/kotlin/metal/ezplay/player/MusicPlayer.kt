package metal.ezplay.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import java.io.File

actual class MusicPlayer(private val exoPlayer: ExoPlayer) {

    actual val isPlaying: Boolean
        get() = exoPlayer.isPlaying

    actual fun play() {
        exoPlayer.playbackState
        exoPlayer.play()
    }

    actual fun play(uri: String) {
        with (exoPlayer) {
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    actual fun pause() {
        exoPlayer.pause()
    }

    actual fun stop() {
        exoPlayer.stop()
    }
}