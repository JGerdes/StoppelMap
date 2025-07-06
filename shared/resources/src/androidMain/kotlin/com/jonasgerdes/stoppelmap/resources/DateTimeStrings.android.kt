package com.jonasgerdes.stoppelmap.resources

import android.content.Context
import com.jonasgerdes.stoppelmap.shared.resources.R
import kotlinx.datetime.format.MonthNames

fun DateTimeStrings.monthNames(context: Context): MonthNames = MonthNames(
    january = "january", //context.getString(R.string.common_datetime_month_january),
    february = "february", //context.getString(R.string.common_datetime_month_february),
    march = "march", //context.getString(R.string.common_datetime_month_march),
    april = "april", //context.getString(R.string.common_datetime_month_april),
    may = "may", //context.getString(R.string.common_datetime_month_may),
    june = "june", //context.getString(R.string.common_datetime_month_june),
    july = "july", //context.getString(R.string.common_datetime_month_july),
    august = "august", //context.getString(R.string.common_datetime_month_august),
    september = "september", //context.getString(R.string.common_datetime_month_september),
    october = "october", //context.getString(R.string.common_datetime_month_october),
    november = "november", //context.getString(R.string.common_datetime_month_november),
    december = "december", //context.getString(R.string.common_datetime_month_december),
)