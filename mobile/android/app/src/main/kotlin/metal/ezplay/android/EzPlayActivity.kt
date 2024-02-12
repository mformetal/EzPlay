package metal.ezplay.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import metal.ezplay.search.SearchScreen
import metal.ezplay.search.SearchViewModel
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
                val navController = rememberNavController()
                val player = get<MusicPlayer>()
                val queue = get<PlayerQueue>()
                val database = get<AppDatabase>()
                val client = get<HttpClient>()

                val nowPlayingViewModel = NowPlayingViewModel(
                    player,
                    queue,
                )

                val libraryPager = Pager(PagingConfig(pageSize = 100, initialLoadSize = 100)) {
                    SongPagingSource(client)
                }
                val libraryViewModel = LibraryViewModel(database, client, queue, player, libraryPager)

                val searchViewModel = SearchViewModel(client, queue)

                AppTheme {
                    Scaffold(
                        bottomBar = {
                            BottomBar(navController, listOf(Screen.Library, Screen.Search))
                        }
                    ) { innerPadding: PaddingValues ->
                        NavHost(navController, startDestination = Screen.Library.route, Modifier.padding(innerPadding)) {
                            composable(Screen.Library.route) {
                                Column {
                                    val libraryModifier = Modifier.weight(.9f)
                                    val nowPlayingModifier = Modifier.weight(.1f)
                                    LibraryScreen(libraryModifier, libraryViewModel, nowPlayingViewModel)
                                    NowPlayingScreen(nowPlayingModifier, nowPlayingViewModel)
                                }
                            }

                            composable(Screen.Search.route) {
                                Column {
                                    val searchModifier = Modifier.weight(.9f)
                                    val nowPlayingModifier = Modifier.weight(.1f)
                                    SearchScreen(searchModifier, searchViewModel)
                                    NowPlayingScreen(nowPlayingModifier, nowPlayingViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}