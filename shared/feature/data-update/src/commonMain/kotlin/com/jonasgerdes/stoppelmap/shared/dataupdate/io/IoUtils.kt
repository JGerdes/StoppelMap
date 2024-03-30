package com.jonasgerdes.stoppelmap.shared.dataupdate.io

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import okio.Sink
import okio.Source
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
            while (!packet.isEmpty) {
                sink.write(packet.readBytes())
            }
        }
    }
}

@Suppress("NAME_SHADOWING")
suspend fun ByteWriteChannel.writeAll(source: Source) {
    val channel = this
    var bytesRead: Int
    val buffer = ByteArray(OKIO_RECOMMENDED_BUFFER_SIZE)

    source.buffer().use { source ->
        while (source.read(buffer).also { bytesRead = it } != -1 && !channel.isClosedForWrite) {
            channel.writeFully(buffer, offset = 0, length = bytesRead)
            channel.flush()
        }
    }
}