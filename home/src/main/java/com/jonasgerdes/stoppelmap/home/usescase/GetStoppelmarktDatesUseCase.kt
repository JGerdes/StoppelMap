package com.jonasgerdes.stoppelmap.home.usescase

import org.threeten.bp.*

class GetStoppelmarktDatesUseCase {

    private val dates = listOf(
        LocalDateTime.of(2019, Month.AUGUST, 15, 18, 0, 0),
        LocalDateTime.of(2020, Month.AUGUST, 13, 18, 0, 0)
    )

    operator fun invoke() = dates
}