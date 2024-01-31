package metal.ezplay.storage

import io.ktor.utils.io.ByteReadChannel
import java.io.File

interface MusicFileStorage {

    suspend fun createFile(fileName: String): File

    suspend fun downloadInto(channel: ByteReadChannel, file: File)
}