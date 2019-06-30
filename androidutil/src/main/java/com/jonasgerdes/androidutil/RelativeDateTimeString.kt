package com.jonasgerdes.androidutil

import android.content.res.Resources
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import org.threeten.bp.temporal.ChronoUnit.DAYS

private val dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

fun LocalDate.asRelativeString(resources: Resources, toDate: LocalDate = LocalDate.now()): String {

    val difference = DAYS.between(this, toDate)
    return when {
        difference == 0L -> resources.getString(R.string.date_today)
        difference == 1L -> resources.getString(R.string.date_yesterday)
        difference < 7L -> this.getWeekdayString(resources)
        difference < 14L -> resources.getQuantityString(R.plurals.date_weeks_ago, 1)
        else -> this.format(dateFormat)
    }
}

private fun LocalDate.getWeekdayString(resources: Resources) = when (dayOfWeek) {
    DayOfWeek.MONDAY -> resources.getString(R.string.date_weekday_monday)
    DayOfWeek.TUESDAY -> resources.getString(R.string.date_weekday_tuesday)
    DayOfWeek.WEDNESDAY -> resources.getString(R.string.date_weekday_wednesday)
    DayOfWeek.THURSDAY -> resources.getString(R.string.date_weekday_thursday)
    DayOfWeek.FRIDAY -> resources.getString(R.string.date_weekday_friday)
    DayOfWeek.SATURDAY -> resources.getString(R.string.date_weekday_saturday)
    DayOfWeek.SUNDAY -> resources.getString(R.string.date_weekday_sunday)
}
