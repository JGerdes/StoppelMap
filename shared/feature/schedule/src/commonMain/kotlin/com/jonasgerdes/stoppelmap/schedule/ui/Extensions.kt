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
    DayOfWeek.MONDAY -> Res.strings.common_datetime_weekday_monday
    DayOfWeek.TUESDAY -> Res.strings.common_datetime_weekday_tuesday
    DayOfWeek.WEDNESDAY -> Res.strings.common_datetime_weekday_wednesday
    DayOfWeek.THURSDAY -> Res.strings.common_datetime_weekday_thursday
    DayOfWeek.FRIDAY -> Res.strings.common_datetime_weekday_friday
    DayOfWeek.SATURDAY -> Res.strings.common_datetime_weekday_saturday
    DayOfWeek.SUNDAY -> Res.strings.common_datetime_weekday_sunday
    else -> throw IllegalArgumentException("Unexpected DayOfWeek: $this")
}