package com.jonasgerdes.stoppelmap.resources

import com.jonasgerdes.stoppelmap.shared.resources.Res
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DayOfWeekNames

actual class FormattedLocalDateTimeStringDesc actual constructor(
    private val dateTime: LocalDateTime,
    private val formatter: (DayOfWeekNames) -> DateTimeFormat<LocalDateTime>
) : StringDesc {
    override fun localized() = dateTime.format(
        formatter(
            DayOfWeekNames(
                monday = Res.strings.common_datetime_weekday_monday.desc().localized(),
                tuesday = Res.strings.common_datetime_weekday_tuesday.desc().localized(),
                wednesday = Res.strings.common_datetime_weekday_wednesday.desc().localized(),
                thursday = Res.strings.common_datetime_weekday_thursday.desc().localized(),
                friday = Res.strings.common_datetime_weekday_friday.desc().localized(),
                saturday = Res.strings.common_datetime_weekday_saturday.desc().localized(),
                sunday = Res.strings.common_datetime_weekday_sunday.desc().localized(),
            )
        )
    )
}