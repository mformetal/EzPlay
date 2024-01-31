package metal.ezplay.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import metal.ezplay.library.LibraryScreen
import metal.ezplay.library.LibraryViewModel
import metal.ezplay.network.EzPlayApi
import metal.ezplay.nowplaying.NowPlayingViewModel
import metal.ezplay.storage.AndroidDriverFactory
import metal.ezplay.storage.AndroidMusicFileStorage
import metal.ezplay.storage.createDatabase

class EzPlayActivity : ComponentActivity() {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
    private val api = EzPlayApi(client)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val driverFactory = AndroidDriverFactory(this)
        val database = createDatabase(driverFactory)
        val libraryViewModel = LibraryViewModel(api, database)
        val extensionRendererMode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER

        val renderersFactory = DefaultRenderersFactory(applicationContext)
            .setExtensionRendererMode(extensionRendererMode)
            .setEnableDecoderFallback(true)
        val trackSelector = DefaultTrackSelector(applicationContext)
        val exoPlayer = ExoPlayer.Builder(applicationContext, renderersFactory)
            .setTrackSelector(trackSelector)
            .build().apply {
                trackSelectionParameters = DefaultTrackSelector.Parameters.Builder(applicationContext).build()
                playWhenReady = false
            }

        val nowPlayingViewModel = NowPlayingViewModel(api, exoPlayer, AndroidMusicFileStorage(filesDir))

        setContent {
            val navController = rememberNavController()
            AppTheme {
                BottomBar(navController) {
                    NavHost(
                        navController = navController,
                        startDestination = "library"
                    ) {
                        composable(route = "library") {
                            LibraryScreen(libraryViewModel, nowPlayingViewModel)
                        }
                    }
                }
            }
        }
    }
}