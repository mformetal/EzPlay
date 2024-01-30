package metal.ezplay.storage

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import java.io.File
import java.io.FileOutputStream


class AndroidMusicFileStorage(private val filesDir: File) : MusicFileStorage {

    override suspend fun download(channel: ByteReadChannel, fileName: String): File = File(filesDir, fileName).apply {
        if (exists()) return@apply

        createNewFile()
        while (!channel.isClosedForRead) {
            val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
            while (!packet.isEmpty) {
                val bytes = packet.readBytes()
                appendBytes(bytes)
            }
        }
    }
}