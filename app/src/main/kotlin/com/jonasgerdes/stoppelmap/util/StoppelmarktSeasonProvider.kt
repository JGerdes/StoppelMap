package com.jonasgerdes.stoppelmap.util

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.base.contract.Season
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import kotlinx.datetime.*

private val startTime = LocalTime(18, 30)
private val endTime = LocalTime(23, 0)

class StoppelmarktSeasonProvider(
    private val clockProvider: ClockProvider,
) : SeasonProvider {

    override fun getCurrentOrNextSeason(): Season {
        val currentYear = clockProvider.nowAsLocalDateTime().year

        val currentYearsSeason = getSeasonForYear(currentYear)

        return if (clockProvider.nowAsLocalDateTime() <= currentYearsSeason.end) {
            currentYearsSeason
        } else {
            getSeasonForYear(currentYear + 1)
        }
    }
}

fun getSeasonForYear(year: Int): Season = StoppelmarktSeason(
    year = year,
    days = calculateDatesForYear(year)
)

data class StoppelmarktSeason(
    override val year: Int,
    override val days: List<LocalDate>
) : Season {
    override val start = days.first().atTime(startTime)
    override val end = days.last().atTime(endTime)
}

fun calculateDatesForYear(year: Int): List<LocalDate> {
    // On 15th of august it's always Stoppelmarkt.
    // If 15th is a wednesday, the 16th will be first day of Stoppelmarkt
    val anchorDay = LocalDate(year, Month.AUGUST, 15)
    val anchorOffset = when (anchorDay.dayOfWeek) {
        DayOfWeek.MONDAY -> -4
        DayOfWeek.TUESDAY -> -5
        DayOfWeek.WEDNESDAY -> 1
        DayOfWeek.THURSDAY -> 0
        DayOfWeek.FRIDAY -> -1
        DayOfWeek.SATURDAY -> -2
        DayOfWeek.SUNDAY -> -3
    }

    return (anchorOffset..anchorOffset + 5).map { offset ->
        anchorDay.plus(offset, DateTimeUnit.DAY)
    }
}
