package com.jonasgerdes.stoppelmap.core.domain

import org.threeten.bp.*


interface DateTimeProvider {
    operator fun invoke(): OffsetDateTime
}

class LiveDateTimeProvider : DateTimeProvider {
    override fun invoke() = OffsetDateTime.now()
}

class NowAtStomaSaturdayTimeProvider : DateTimeProvider {
    override fun invoke() = LocalDate.of(2019, Month.AUGUST, 17)
        .atTime(LocalTime.now())
        .atZone(ZoneId.systemDefault())
        .toOffsetDateTime()
}