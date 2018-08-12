package com.jonasgerdes.stoppelmap.bus.station

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.model.transportation.Departure
import com.jonasgerdes.stoppelmap.model.transportation.Station
import com.jonasgerdes.stoppelmap.model.transportation.getForStationAfter
import com.jonasgerdes.stoppelmap.util.DateTimeProvider
import com.jonasgerdes.stoppelmap.util.toLiveData
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.threeten.bp.DayOfWeek
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class StationViewModel : ViewModel() {

    private val database: StoppelMapDatabase by inject()
    private val dateTimeProvider: DateTimeProvider by inject()

    private val stateRelay = BehaviorRelay.create<StationState>()
    private var routeDisposable: Disposable? = null

    val format = DateTimeFormatter.ofPattern("HH")

    val state: LiveData<StationState> = stateRelay.toLiveData()


    fun setStation(stationSlug: String) {
        routeDisposable?.dispose()

        routeDisposable = Observable.interval(0, 30, TimeUnit.SECONDS)
                .map {
                    val departures = database.departures().getAllByStation(stationSlug)

                    departures.groupBy { getSpanFor(it.time.hour) }
                            .map {
                                TimeSpan(type = it.key, slots = it.value.groupBy {
                                    it.time.format(format)
                                }.map {
                                    TimeSlot(label = it.key, days = createDays(it.value.groupBy {
                                        getDayFor(it.time)
                                    }))
                                }.sortedBy { it.label })
                            }
                            .sortedBy { it.type.ordinal }

                }
                .map { it.toGridItems() }
                .map { StationState(departureItems = it) }
                .subscribe(stateRelay)
    }

    private fun createDays(departureGroups: Map<DayOfWeek, List<Departure>>) =
            Day.values().map {
                DayDepartures(
                        day = it,
                        departures = departureGroups[it.dayOfWeek]?.map { it.time } ?: emptyList())
            }.sortedBy { it.day.ordinal }


    fun getDayFor(dateTime: OffsetDateTime) = if (dateTime.hour < 5) {
        dateTime.minusDays(1).dayOfWeek
    } else {
        dateTime.dayOfWeek
    }

    fun getSpanFor(hour: Int) = when (hour) {
        in 5..12 -> TimeSpan.Type.MORNING
        in 13..17 -> TimeSpan.Type.AFTERNOON
        in 18..22 -> TimeSpan.Type.EVENING
        else -> TimeSpan.Type.NIGHT
    }


}