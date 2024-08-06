package com.jonasgerdes.stoppelmap.transportation.usecase

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.transportation.data.TransportDataSource
import com.jonasgerdes.stoppelmap.transportation.model.DepartureTime
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus

class GetNextDeparturesUseCase(
    private val clockProvider: ClockProvider,
    private val transportDataSource: TransportDataSource,
) {

    suspend operator fun invoke(
        stationSlug: String,
        maxDepartures: Int = 3,
        now: LocalDateTime = clockProvider.nowAsLocalDateTime(),
    ): List<DepartureTime> {
        return transportDataSource.getDeparturesAfterByStation(
            stationSlug = stationSlug,
            after = now,
            limit = maxDepartures.toLong()
        )
            .map {
                with(clockProvider) {
                    val difference = it.time.asInstant() - now.asInstant()
                    when {
                        difference.inWholeMinutes < 1 ->
                            DepartureTime.Immediately

                        difference.inWholeMinutes < 30 ->
                            DepartureTime.InMinutes(difference.inWholeMinutes.toInt())

                        it.time.isConsideredSameDay(now.date) ->
                            DepartureTime.Today(it.time.time)

                        it.time.isConsideredSameDay(now.date.nextDay()) ->
                            DepartureTime.Tomorrow(it.time.time)

                        difference.inWholeDays < 7 ->
                            DepartureTime.ThisWeek(it.time)

                        else -> DepartureTime.Absolute(dateTime = it.time)
                    }
                }
            }
    }
}


private fun LocalDate.nextDay() = plus(1, DateTimeUnit.DAY)

private fun LocalDateTime.isConsideredSameDay(other: LocalDate) =
// Today or tomorrow but in the night so we consider it today still.
    // TODO: Make this calculation somehow consistent throughout the app & preparation
    date == other || date == other.plus(1, DateTimeUnit.DAY) && time.hour < 4
