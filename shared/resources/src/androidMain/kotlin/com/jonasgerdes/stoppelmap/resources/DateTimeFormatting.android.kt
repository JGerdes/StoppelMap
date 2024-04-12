package com.jonasgerdes.stoppelmap.resources

import android.content.Context
import com.jonasgerdes.stoppelmap.shared.resources.R
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DayOfWeekNames

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