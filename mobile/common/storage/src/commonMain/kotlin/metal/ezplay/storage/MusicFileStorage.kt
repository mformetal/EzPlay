package metal.ezplay.storage

import io.ktor.utils.io.ByteReadChannel
import java.io.File

interface MusicFileStorage {

    suspend fun download(channel: ByteReadChannel, fileName: String): File
}