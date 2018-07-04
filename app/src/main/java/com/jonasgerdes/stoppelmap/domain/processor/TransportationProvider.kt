package com.jonasgerdes.stoppelmap.domain.processor

import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.domain.model.TransportRoute
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.model.Converters
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.util.DateTimeProvider
import io.reactivex.Observable
import org.threeten.bp.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class TransportationProvider
    : BaseOperation<TransportationProvider.Action>(Action::class.java) {

    private val database: StoppelMapDatabase by inject()
    private val dateTimeProvider: DateTimeProvider by inject()

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.switchMap {
        Observable.interval(30, TimeUnit.SECONDS)
                .startWith(0)
                .switchMap {
                    database.routes()
                            .getAll()
                            .map {
                                it.map {
                                    TransportRoute(
                                            slug = it.slug,
                                            name = it.name,
                                            nearestStations = getNearestStations(it.slug)
                                    )
                                }
                            }
                            .map { Result(it) }
                            .toObservable()
                }
    }

    private fun getNearestStations(route: String): List<TransportRoute.Station> {
        val time = dateTimeProvider.now()
        val timeString = Converters.fromOffsetDateTime(time)!!
        return database.stations().getAllByRoute(route)
                .take(3)
                .map {
                    TransportRoute.Station(it.name,
                            database.departures()
                                    .getForStationAfter(it.slug, timeString)
                                    .firstOrNull()
                                    ?.let {
                                        ChronoUnit.MILLIS.between(time, it.time)
                                    } ?: -1
                    )
                }
    }

    class Action : BaseAction

    data class Result(val routes: List<TransportRoute>) : BaseResult
}