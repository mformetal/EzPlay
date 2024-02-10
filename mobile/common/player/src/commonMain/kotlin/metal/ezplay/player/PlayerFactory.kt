package metal.ezplay.player

import org.koin.core.scope.Scope

expect fun Scope.createMusicPlayer(): MusicPlayer

expect fun Scope.createSongDownloader(): SongDownloader