package com.jonasgerdes.stoppelmap.resources

import com.jonasgerdes.stoppelmap.shared.resources.Res
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

expect class FormattedLocalDateTimeStringDesc(
    dateTime: LocalDateTime,
    formatter: (DayOfWeekNames) -> DateTimeFormat<LocalDateTime>
) : StringDesc

expect class FormattedLocalDateStringDesc(
    date: LocalDate,
    formatter: (MonthNames) -> DateTimeFormat<LocalDate>
) : StringDesc


fun LocalDate.defaultFormat() = FormattedLocalDateStringDesc(this) {
    LocalDate.Format {
        dayOfMonth(Padding.NONE)
        chars(". ")
        monthName(it)
        char(' ')
        year()
    }
}

fun LocalDate.defaultFormatWithoutYear() = FormattedLocalDateStringDesc(this) {
    LocalDate.Format {
        dayOfMonth(Padding.NONE)
        chars(". ")
        monthName(it)
    }
}

fun LocalDate.dayOfMonthFormat() = FormattedLocalDateStringDesc(this) {
    LocalDate.Format {
        dayOfMonth(padding = Padding.NONE)
        char('.')
    }
}

fun LocalDateTime.defaultFormat() = FormattedLocalDateTimeStringDesc(this) {
    LocalDateTime.Format {
        dayOfWeek(it)
        chars(" ,")
        hour()
        char(':')
        minute()
    }
}

fun LocalTime.Companion.defaultFormat() = LocalTime.Format {
    hour()
    char(':')
    minute()
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