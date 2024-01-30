package metal.ezplay.nowplaying

import android.net.Uri
import androidx.media3.common.C
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.TransferListener
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import io.ktor.utils.io.read
import java.io.InputStream
import kotlin.math.min

class ByteReadChannelDataSource(
    private val channel: ByteReadChannel,
    private val totalSize: Long) : DataSource {
        
    private var readPosition = 0L
    private var bytesRemaining = 0
    private var stream: InputStream ?= null

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        if (length == 0) return 0
        if (bytesRemaining == 0) return C.RESULT_END_OF_INPUT
        
        val bytesRead = requireNotNull(stream).read(buffer, offset, min(length, bytesRemaining))
        readPosition += bytesRead
        bytesRemaining
        bytesRemaining -= length
        return bytesRead
    }

    override fun addTransferListener(transferListener: TransferListener) {
        TODO("Not yet implemented")
    }

    override fun open(dataSpec: DataSpec): Long {
        return totalSize.also {
            readPosition = dataSpec.position
            bytesRemaining = totalSize.toInt() - dataSpec.position.toInt()
            
            if (stream == null) {
                stream = channel.toInputStream()
            }
        }
    }

    override fun getUri(): Uri? = null

    override fun close() {
        stream?.close()
        stream = null
    }
}