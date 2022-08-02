package com.jonasgerdes.stoppelmap.transportation.ui.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RouteViewModel(
    routeId: String,
    busRoutesRepository: BusRoutesRepository
) : ViewModel() {

    private val routeState = busRoutesRepository.getRouteById(routeId)
        .map { route ->
            BusRouteDetails(
                routeId = route.id,
                title = route.title,
                stations = route.stations.map { station ->
                    if (station.isDestination) {
                        BusRouteDetails.Station.Destination(
                            id = station.id,
                            title = station.title
                        )
                    } else {
                        BusRouteDetails.Station.Stop(
                            id = station.id,
                            title = station.title,
                            nextDepartures = station.departures.first().departures.take(3).map {
                                BusRouteDetails.DepartureTime.Absolut(
                                    time = it.time
                                )
                            },
                            annotateAsNew = station.annotateAsNew
                        )
                    }
                },
                additionalInfo = route.additionalInfo
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
