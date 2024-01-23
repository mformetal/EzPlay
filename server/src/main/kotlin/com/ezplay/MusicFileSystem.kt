package com.ezplay

import com.ezplay.extensions.hasSongExtension
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.AudioHeader
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import java.nio.file.FileVisitResult
import kotlin.io.path.Path
import kotlin.io.path.name
import kotlin.io.path.visitFileTree
import kotlin.io.path.walk


class MusicFileSystem {

    private val rootDirectory = Path(System.getProperty("user.home"), "Music")

    suspend fun songs(): Sequence<SongMetadata> =
        rootDirectory.walk()
            .filter { path ->
                path.hasSongExtension
            }
            .map { path ->
                AudioFileIO.read(path.toFile()).tag.run {
                    SongMetadata(
                        songName = getFirst(FieldKey.TITLE)
                    )
                }
            }
}