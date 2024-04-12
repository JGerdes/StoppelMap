package com.jonasgerdes.stoppelmap.resources

import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DayOfWeekNames

expect class FormattedLocalDateTimeStringDesc(
    dateTime: LocalDateTime,
    formatter: (DayOfWeekNames) -> DateTimeFormat<LocalDateTime>
) : StringDesc