package com.jonasgerdes.stoppelmap.domain

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

fun calculateDatesForYear(year: Int): List<LocalDate> {
    // On 15th of august it's always Stoppelmarkt.
    // If 15th is a wednesday, the 16th will be first day of Stoppelmarkt
    val anchorDay = LocalDate.of(year, Month.AUGUST, 15)
    val anchorOffset = when (anchorDay.dayOfWeek) {
        DayOfWeek.MONDAY -> -4
        DayOfWeek.TUESDAY -> -5
        DayOfWeek.WEDNESDAY -> 1
        DayOfWeek.THURSDAY -> 0
        DayOfWeek.FRIDAY -> -1
        DayOfWeek.SATURDAY -> -2
        DayOfWeek.SUNDAY -> -3
    }

    return (anchorOffset .. anchorOffset + 5).map { offset ->
        anchorDay.plusDays((offset).toLong())
    }
}