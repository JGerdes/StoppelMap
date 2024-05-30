package com.jonasgerdes.stoppelmap.resources

import android.content.Context
import com.jonasgerdes.stoppelmap.shared.resources.R
import kotlinx.datetime.format.MonthNames

fun DateTimeStrings.monthNames(context: Context): MonthNames = MonthNames(
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