@file:JvmName("PlayerFactoryKt")

package metal.ezplay.player

import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

val playerModule = module {
    single { createMusicPlayer() }
    single { createSongDownloader() }
    single {
        PlayerQueue(
            scope = GlobalScope,
            mainDispatcher = Dispatchers.Main,
            backgroundDispatcher = Dispatchers.IO,
            player = get<MusicPlayer>(),
            client = get<HttpClient>(),
            downloader = get<SongDownloader>()
        )
    }
}
