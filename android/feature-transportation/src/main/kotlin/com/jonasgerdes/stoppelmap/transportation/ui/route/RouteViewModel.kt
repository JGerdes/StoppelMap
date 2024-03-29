package com.jonasgerdes.stoppelmap.transportation.ui.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails.ReturnStation
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails.Station
import com.jonasgerdes.stoppelmap.transportation.usecase.GetNextDeparturesUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class RouteViewModel(
    routeId: String,
    busRoutesRepository: BusRoutesRepository,
    private val clockProvider: ClockProvider,
    private val getNextDepartures: GetNextDeparturesUseCase
) : ViewModel() {

    private val timeUpdate = flow {
        while (true) {
            emit(clockProvider.nowAsLocalDateTime())
            delay(10_000)
        }
    }
    private val routeState =
        combine(
            busRoutesRepository.getRouteById(routeId),
            timeUpdate
        ) { route, now ->
            BusRouteDetails(
                routeId = route.id,
                title = route.title,
                stations = route.stations.map { station ->
                    if (station.isDestination) {
                        Station.Destination(
                            id = station.id,
                            title = station.title
                        )
                    } else {
                        Station.Stop(
                            id = station.id,
                            title = station.title,
                            nextDepartures = getNextDepartures(
                                departures = station.departures.flatMap { it.departures },
                                now = now
                            ),
                            annotateAsNew = station.annotateAsNew
                        )
                    }
                },
                additionalInfo = route.additionalInfo,
                returnStations = route.returnStations.map {
                    ReturnStation(
                        id = it.id,
                        title = it.title,
                    )
                }
            )
        }
            .map(RouteState::Loaded)

    val state: StateFlow<ViewState> =
        routeState
            .map(::ViewState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState.Default
            )

    data class ViewState(
        val routeState: RouteState,
    ) {

        companion object {
            val Default = ViewState(
                routeState = RouteState.Loading
            )
        }
    }

    sealed class RouteState {
        object Loading : RouteState()
        data class Loaded(val routeDetails: BusRouteDetails) : RouteState()
    }
}
