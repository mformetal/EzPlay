package metal.ezplay.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.ktor.client.HttpClient
import metal.ezplay.android.compose.AppTheme
import metal.ezplay.library.LibraryScreen
import metal.ezplay.library.LibraryViewModel
import metal.ezplay.library.SongPagingSource
import metal.ezplay.network.networkModule
import metal.ezplay.nowplaying.NowPlayingScreen
import metal.ezplay.nowplaying.NowPlayingViewModel
import metal.ezplay.player.MusicPlayer
import metal.ezplay.player.PlayerQueue
import metal.ezplay.player.playerModule
import metal.ezplay.storage.AppDatabase
import metal.ezplay.storage.databaseModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication

class EzPlayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KoinApplication(application = {
                androidContext(application)
                modules(networkModule, playerModule, databaseModule)
            }) {
                val player = get<MusicPlayer>()
                val queue = get<PlayerQueue>()
                val database = get<AppDatabase>()
                val client = get<HttpClient>()

                val nowPlayingViewModel = NowPlayingViewModel(
                    player,
                    queue,
                )

                val pagingConfig = PagingConfig(pageSize = 100, initialLoadSize = 100)
                val pager = Pager(pagingConfig) {
                    SongPagingSource(client)
                }
                val libraryViewModel = LibraryViewModel(database, client, queue, player, pager)

                AppTheme {
                    Column {
                        val libraryModifier = Modifier.weight(.9f)
                        val nowPlayingModifier = Modifier.weight(.1f)
                        LibraryScreen(libraryModifier, libraryViewModel, nowPlayingViewModel)
                        NowPlayingScreen(nowPlayingModifier, nowPlayingViewModel)
                    }
                }
            }
        }
    }
}