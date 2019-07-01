package com.jonasgerdes.stoppelmap.core.domain

import org.threeten.bp.OffsetDateTime


interface DateTimeProvider {
    operator fun invoke(): OffsetDateTime
}

class LiveDateTimeProvider : DateTimeProvider {
    override fun invoke() = OffsetDateTime.now()

}