package com.jonasgerdes.stoppelmap.util

import org.threeten.bp.*


object DateTimeProvider {
    fun now() = realTime()

    private fun realTime() = OffsetDateTime.now()
    private fun fakeTime() = OffsetDateTime.of(
            LocalDateTime.of(
                    LocalDate.of(2018, Month.AUGUST, 18), LocalTime.now()
            ),
            OffsetDateTime.now().offset
    )
}