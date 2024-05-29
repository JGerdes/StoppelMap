package com.jonasgerdes.stoppelmap.provider

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.base.contract.Season
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.atTime
import kotlinx.datetime.plus

private val startTime = LocalTime(18, 30)
private val endTime = LocalTime(23, 0)

// Based on https://www.vechta.de/nachricht/schaustellerverein-stellt-tore-auf
private val iterationBaseYear = 1998
private val iterationBaseCount = 700
private val skippedYears = setOf(2020, 2021)

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

    override fun getThisYearsSeason(): Season =
        getSeasonForYear(clockProvider.nowAsLocalDateTime().year)
}

fun getSeasonForYear(year: Int): Season = StoppelmarktSeason(
    year = year,
    iteration = calculateIteration(year),
    days = calculateDatesForYear(year)
)

fun calculateIteration(year: Int): Int {
    // Simplified, doesn't support years < 2022 yet
    return (year - iterationBaseYear) + iterationBaseCount - skippedYears.size
}

data class StoppelmarktSeason(
    override val year: Int,
    override val iteration: Int,
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
        else -> throw IllegalArgumentException("Invalid weekday: ${anchorDay.dayOfWeek}")
    }

    return (anchorOffset..anchorOffset + 5).map { offset ->
        anchorDay.plus(offset, DateTimeUnit.DAY)
    }
}
