package com.jonasgerdes.stoppelmap.shared.dataupdate.io

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import okio.Sink
import okio.buffer
import okio.use

private const val OKIO_RECOMMENDED_BUFFER_SIZE: Int = 8192

@Suppress("NAME_SHADOWING")
suspend fun ByteReadChannel.readFully(sink: Sink) {
    val channel = this
    sink.buffer().use { sink ->
        while (!channel.isClosedForRead) {
            // TODO: Allocating a new packet on every copy isn't great. Find a faster way to move bytes.
            val packet = channel.readRemaining(OKIO_RECOMMENDED_BUFFER_SIZE.toLong())
            while (!packet.exhausted()) {
                sink.write(packet.readByteArray())
            }
        }
    }
}