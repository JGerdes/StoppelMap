package com.jonasgerdes.stoppelmap.bus.route

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.model.transportation.getForStationAfter
import com.jonasgerdes.stoppelmap.util.DateTimeProvider
import com.jonasgerdes.stoppelmap.util.toLiveData
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class RouteViewModel : ViewModel() {

    private val database: StoppelMapDatabase by inject()
    private val dateTimeProvider: DateTimeProvider by inject()

    private val stateRelay = BehaviorRelay.create<RouteState>()
    private var routeDisposable: Disposable? = null

    val state: LiveData<RouteState> = stateRelay.toLiveData()


    fun setRoute(routeSlug: String) {
        routeDisposable?.dispose()

        routeDisposable = Observable.interval(0, 30, TimeUnit.SECONDS)
                .map { database.stations().getAllByRoute(routeSlug) }
                .map { stations ->
                    val time = dateTimeProvider.now()
                    stations.map {
                        TransportStation(it.slug, it.name,
                                database.departures()
                                        .getForStationAfter(it.slug, time)
                                        .map { it.millisUntil(time) }
                        )
                    }
                }
                .map { RouteState(stations = it) }
                .subscribe(stateRelay)
    }

    override fun onCleared() {
        routeDisposable?.dispose()
        super.onCleared()
    }
}