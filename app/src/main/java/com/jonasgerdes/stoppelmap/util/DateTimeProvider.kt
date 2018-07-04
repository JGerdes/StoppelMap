package com.jonasgerdes.stoppelmap.util

import org.threeten.bp.*


object DateTimeProvider {
    fun now() = OffsetDateTime.of(
            LocalDateTime.of(
                    LocalDate.of(2016, Month.AUGUST, 13), LocalTime.now()
            ),
            OffsetDateTime.now().offset
    )
}