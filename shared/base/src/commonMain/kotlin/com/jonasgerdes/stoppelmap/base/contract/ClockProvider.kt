package com.jonasgerdes.stoppelmap.base.contract

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime


interface ClockProvider {

    fun nowAsInstant(): Instant

    fun nowAsLocalDateTime(): LocalDateTime

    fun toLocalDateTime(instant: Instant): LocalDateTime

    fun toInstant(localDateTime: LocalDateTime): Instant


    fun Instant.asLocalDateTime() = toLocalDateTime(this)
    fun LocalDateTime.asInstant() = toInstant(this)
}
