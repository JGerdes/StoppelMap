package com.jonasgerdes.stoppelmap.schedule.ui

import com.jonasgerdes.stoppelmap.shared.resources.Res
import kotlinx.datetime.DayOfWeek

fun DayOfWeek.toShortResourceString() = when (this) {
    DayOfWeek.MONDAY -> Res.strings.schedule_day_short_monday
    DayOfWeek.TUESDAY -> Res.strings.schedule_day_short_tuesday
    DayOfWeek.WEDNESDAY -> Res.strings.schedule_day_short_wednesday
    DayOfWeek.THURSDAY -> Res.strings.schedule_day_short_thursday
    DayOfWeek.FRIDAY -> Res.strings.schedule_day_short_friday
    DayOfWeek.SATURDAY -> Res.strings.schedule_day_short_saturday
    DayOfWeek.SUNDAY -> Res.strings.schedule_day_short_sunday
    else -> throw IllegalArgumentException("Unexpected DayOfWeek: $this")
}

fun DayOfWeek.toFullResourceString() = when (this) {
    DayOfWeek.MONDAY -> Res.strings.schedule_day_full_monday
    DayOfWeek.TUESDAY -> Res.strings.schedule_day_full_tuesday
    DayOfWeek.WEDNESDAY -> Res.strings.schedule_day_full_wednesday
    DayOfWeek.THURSDAY -> Res.strings.schedule_day_full_thursday
    DayOfWeek.FRIDAY -> Res.strings.schedule_day_full_friday
    DayOfWeek.SATURDAY -> Res.strings.schedule_day_full_saturday
    DayOfWeek.SUNDAY -> Res.strings.schedule_day_full_sunday
    else -> throw IllegalArgumentException("Unexpected DayOfWeek: $this")
}