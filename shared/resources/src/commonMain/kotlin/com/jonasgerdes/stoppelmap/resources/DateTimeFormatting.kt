package com.jonasgerdes.stoppelmap.resources

import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
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