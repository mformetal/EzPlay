@file:JvmName("PlayerFactoryKt")

package metal.ezplay.player

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.koin.core.scope.Scope
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
            downloader = get<SongDownloader>()
        )
    }
}