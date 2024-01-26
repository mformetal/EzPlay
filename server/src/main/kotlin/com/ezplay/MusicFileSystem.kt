package com.ezplay

import com.ezplay.extensions.hasSongExtension
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import kotlin.io.path.Path
import kotlin.io.path.walk


class MusicFileSystem {

    private val rootDirectory = Path(System.getProperty("user.home"), "Music")

    fun songs(): Sequence<SongMetadata> =
        rootDirectory.walk()
            .filter { path ->
                path.hasSongExtension
            }
            .map { path ->
                val audioFile = AudioFileIO.read(path.toFile())
                audioFile.tag.run {
                    SongMetadata(
                        path = path.toString(),
                        songName = getFirst(FieldKey.TITLE).trim(),
                        songDuration = audioFile.audioHeader.trackLength,
                        artistName = getFirst(FieldKey.ARTIST).trim(),
                        albumName = getFirst(FieldKey.ALBUM).trim()
                    )
                }
            }
}