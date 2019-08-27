package com.jonasgerdes.stoppelmap.transport.usecase

import com.jonasgerdes.stoppelmap.core.domain.DateTimeProvider
import com.jonasgerdes.stoppelmap.data.repository.RouteRepository
import com.jonasgerdes.stoppelmap.model.transportation.Station
import org.threeten.bp.DayOfWeek
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

private val HOUR_FORMAT = DateTimeFormatter.ofPattern("HH")

class GetFullStationUseCase(
    private val routeRepository: RouteRepository,
    private val getCurrentTime: DateTimeProvider
) {
    suspend operator fun invoke(stationSlug: String): FullStation {
        val station = routeRepository.getStationBySlug(stationSlug)!!

        val now = getCurrentTime()
        val departures = routeRepository.getDeparturesByStation(stationSlug).map {
            Departure(it.time, it.time.isBefore(now))
        }
        val prices = routeRepository.getPricesByStation(stationSlug).map { Price(it.type, it.price) }

        val dayTimeSlots = departures.groupBy { getSlotFor(it.time.hour) }
            .map {
                DayTimeSlot(type = it.key, slots = it.value.groupBy {
                    it.time.toOffsetTime().truncatedTo(ChronoUnit.HOURS)
                }.map {
                    TimeSpan(
                        hour = it.key,
                        label = it.key.format(HOUR_FORMAT),
                        departures = createDays(it.value.groupBy {
                            getDayFor(it.time)
                        })
                    )
                }.sortedBy { getFixedHour(it.hour.hour) })
            }
            .sortedBy { it.type.ordinal }

        return FullStation(
            basicInfo = station,
            prices = prices,
            departures = dayTimeSlots
        )
    }

    private fun getFixedHour(time: Int): Int = if (time < 5) time + 24 else time

    private fun createDays(departureGroups: Map<DayOfWeek, List<Departure>>) =
        Day.values().map {
            DayDepartureList(
                day = it,
                departures = departureGroups[it.dayOfWeek] ?: emptyList())
        }.sortedBy { it.day.ordinal }


    private fun getDayFor(dateTime: OffsetDateTime) = if (dateTime.hour < 5) {
        dateTime.minusDays(1).dayOfWeek
    } else {
        dateTime.dayOfWeek
    }

    private fun getSlotFor(hour: Int) = when (hour) {
        in 5..12 -> DayTimeSlot.Type.MORNING
        in 13..17 -> DayTimeSlot.Type.AFTERNOON
        in 18..22 -> DayTimeSlot.Type.EVENING
        else -> DayTimeSlot.Type.NIGHT
    }

    data class FullStation(
        val basicInfo: Station,
        val prices: List<Price>,
        val departures: List<DayTimeSlot>
    )

    data class Price(
        val type: String,
        val price: Int
    )

    data class DayTimeSlot(
        val type: Type,
        val slots: List<TimeSpan>
    ) {
        enum class Type {
            MORNING, AFTERNOON, EVENING, NIGHT
        }
    }

    data class TimeSpan(
        val label: String,
        val hour: OffsetTime,
        val departures: List<DayDepartureList>
    )

    data class DayDepartureList(
        val day: Day,
        val departures: List<Departure>
    )

    enum class Day(val dayOfWeek: DayOfWeek) {
        Thursday(DayOfWeek.THURSDAY),
        Friday(DayOfWeek.FRIDAY),
        Saturday(DayOfWeek.SATURDAY),
        Sunday(DayOfWeek.SUNDAY),
        Monday(DayOfWeek.MONDAY),
        Tuesday(DayOfWeek.TUESDAY)
    }

    data class Departure(
        val time: OffsetDateTime,
        val isPast: Boolean
    )
}