package com.jonasgerdes.stoppelmap.server.monitoring

import io.micrometer.core.instrument.Timer
import kotlinx.datetime.Clock
import kotlin.time.toJavaDuration

suspend fun Timer.coRecord(block: suspend () -> Unit) {
    val start = Clock.System.now()
    block()
    val end = Clock.System.now()
    record((end - start).toJavaDuration())
}