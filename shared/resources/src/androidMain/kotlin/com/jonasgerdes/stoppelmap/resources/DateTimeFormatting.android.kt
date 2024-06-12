package com.jonasgerdes.stoppelmap.resources

import android.content.Context
import com.jonasgerdes.stoppelmap.shared.resources.R
import dev.icerock.moko.resources.desc.StringDesc
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
    override fun toString(context: Context) =
        dateTime.format(
            formatter(
                DayOfWeekNames(
                    monday = context.getString(R.string.common_datetime_weekday_monday),
                    tuesday = context.getString(R.string.common_datetime_weekday_tuesday),
                    wednesday = context.getString(R.string.common_datetime_weekday_wednesday),
                    thursday = context.getString(R.string.common_datetime_weekday_thursday),
                    friday = context.getString(R.string.common_datetime_weekday_friday),
                    saturday = context.getString(R.string.common_datetime_weekday_saturday),
                    sunday = context.getString(R.string.common_datetime_weekday_sunday),
                )
            )
        )
}

actual class FormattedLocalDateStringDesc actual constructor(
    private val date: LocalDate,
    private val formatter: (MonthNames) -> DateTimeFormat<LocalDate>
) : StringDesc {
    override fun toString(context: Context) =
        date.format(
            formatter(
                MonthNames(
                    january = context.getString(R.string.common_datetime_month_january),
                    february = context.getString(R.string.common_datetime_month_february),
                    march = context.getString(R.string.common_datetime_month_march),
                    april = context.getString(R.string.common_datetime_month_april),
                    may = context.getString(R.string.common_datetime_month_may),
                    june = context.getString(R.string.common_datetime_month_june),
                    july = context.getString(R.string.common_datetime_month_july),
                    august = context.getString(R.string.common_datetime_month_august),
                    september = context.getString(R.string.common_datetime_month_september),
                    october = context.getString(R.string.common_datetime_month_october),
                    november = context.getString(R.string.common_datetime_month_november),
                    december = context.getString(R.string.common_datetime_month_december),
                )
            )
        )
}