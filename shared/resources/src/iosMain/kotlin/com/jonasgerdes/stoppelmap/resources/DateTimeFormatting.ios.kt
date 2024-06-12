package com.jonasgerdes.stoppelmap.resources

import com.jonasgerdes.stoppelmap.shared.resources.Res
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames

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

actual class FormattedLocalDateStringDesc actual constructor(
    private val date: LocalDate,
    private val formatter: (MonthNames) -> DateTimeFormat<LocalDate>
) : StringDesc {
    override fun localized() = date.format(
        formatter(
            MonthNames(
                january = Res.strings.common_datetime_month_january.desc().localized(),
                february = Res.strings.common_datetime_month_february.desc().localized(),
                march = Res.strings.common_datetime_month_march.desc().localized(),
                april = Res.strings.common_datetime_month_april.desc().localized(),
                may = Res.strings.common_datetime_month_may.desc().localized(),
                june = Res.strings.common_datetime_month_june.desc().localized(),
                july = Res.strings.common_datetime_month_july.desc().localized(),
                august = Res.strings.common_datetime_month_august.desc().localized(),
                september = Res.strings.common_datetime_month_september.desc().localized(),
                october = Res.strings.common_datetime_month_october.desc().localized(),
                november = Res.strings.common_datetime_month_november.desc().localized(),
                december = Res.strings.common_datetime_month_december.desc().localized(),
            )
        )
    )
}