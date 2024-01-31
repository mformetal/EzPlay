package metal.ezplay.storage

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import metal.ezplay.dto.SongDto
import java.io.File
import java.io.FileOutputStream


class AndroidMusicFileStorage(private val filesDir: File) : MusicFileStorage {
    override suspend fun createFile(fileName: String): File = File(filesDir, fileName).apply {
        if (!exists()) createNewFile()
    }

    override suspend fun downloadInto(channel: ByteReadChannel, file: File) {
        while (!channel.isClosedForRead) {
            val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
            while (!packet.isEmpty) {
                val bytes = packet.readBytes()
                file.appendBytes(bytes)
            }
        }
    }
}