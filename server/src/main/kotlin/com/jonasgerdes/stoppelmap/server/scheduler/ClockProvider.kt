package com.jonasgerdes.stoppelmap.server.scheduler

import kotlinx.datetime.Instant

fun interface ClockProvider {
    fun now(): Instant
}