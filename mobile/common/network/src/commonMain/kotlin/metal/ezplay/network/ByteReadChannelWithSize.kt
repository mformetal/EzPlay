package metal.ezplay.network

import io.ktor.utils.io.ByteReadChannel

class ByteReadChannelWithSize(
    val channel: ByteReadChannel,
    val size: Long
)