package com.jonasgerdes.stoppelmap.transportation.ui

import com.jonasgerdes.stoppelmap.resources.FormattedLocalDateTimeStringDesc
import com.jonasgerdes.stoppelmap.shared.resources.Res
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails
import com.jonasgerdes.stoppelmap.transportation.model.Timetable
import dev.icerock.moko.resources.desc.Raw
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.char

val departureDateTimeFormatter = LocalDateTime.Format {
    dayOfMonth()
    char('.')
    monthNumber()
    chars(", ")
    hour()
    char(':')
    minute()
}

fun departureWeekdayTimeFormatter(dayOfWeekNames: DayOfWeekNames) = LocalDateTime.Format {
    dayOfWeek(dayOfWeekNames)
    chars(", ")
    hour()
    char(':')
    minute()
}

val departureTimeFormatter = LocalTime.Format {
    hour()
    char(':')
    minute()
}

fun BusRouteDetails.DepartureTime.getFormattedStringRes(): StringDesc =
    when (this) {
        BusRouteDetails.DepartureTime.Immediately ->
            StringDesc.Resource(Res.strings.transportation_route_card_next_departures_immediately)

        is BusRouteDetails.DepartureTime.InMinutes ->
            Res.strings.transportation_route_card_next_departures_inMinutes.format(minutes)

        is BusRouteDetails.DepartureTime.Today -> StringDesc.Raw(time.format(departureTimeFormatter))

        is BusRouteDetails.DepartureTime.Tomorrow ->
            Res.strings.transportation_route_card_next_departures_tomorrow.format(
                time.format(departureTimeFormatter)
            )

        is BusRouteDetails.DepartureTime.ThisWeek ->
            FormattedLocalDateTimeStringDesc(
                dateTime = dateTime,
                formatter = ::departureWeekdayTimeFormatter,
            )

        is BusRouteDetails.DepartureTime.Absolute -> StringDesc.Raw(
            dateTime.format(
                departureDateTimeFormatter
            )
        )
    }


fun Timetable.DaySegmentType.toStringResource() = when (this) {
    Timetable.DaySegmentType.MORNING -> Res.strings.transportation_station_timetable_segment_morning
    Timetable.DaySegmentType.AFTERNOON -> Res.strings.transportation_station_timetable_segment_afternoon
    Timetable.DaySegmentType.EVENING -> Res.strings.transportation_station_timetable_segment_evening
    Timetable.DaySegmentType.NIGHT -> Res.strings.transportation_station_timetable_segment_night
}

fun DayOfWeek.toStringResource() = when (this) {
    DayOfWeek.MONDAY -> Res.strings.transportation_station_timetable_day_monday
    DayOfWeek.TUESDAY -> Res.strings.transportation_station_timetable_day_tuesday
    DayOfWeek.WEDNESDAY -> Res.strings.transportation_station_timetable_day_wednesday
    DayOfWeek.THURSDAY -> Res.strings.transportation_station_timetable_day_thursday
    DayOfWeek.FRIDAY -> Res.strings.transportation_station_timetable_day_friday
    DayOfWeek.SATURDAY -> Res.strings.transportation_station_timetable_day_saturday
    DayOfWeek.SUNDAY -> Res.strings.transportation_station_timetable_day_sunday
    else -> throw IllegalArgumentException("Invalid DayOfWeek $this")
}