package com.jonasgerdes.stoppelmap.transportation.ui.route

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails.Station.NextDepartures
import com.jonasgerdes.stoppelmap.transportation.usecase.GetNextDeparturesUseCase
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class RouteViewModel(
    routeId: String,
    busRoutesRepository: BusRoutesRepository,
    private val clockProvider: ClockProvider,
    private val getNextDepartures: GetNextDeparturesUseCase
) : KMMViewModel() {

    private val timeUpdate = flow {
        while (true) {
            emit(clockProvider.nowAsLocalDateTime())
            delay(20.seconds)
        }
    }
    val state: StateFlow<ViewState> =
        combine(
            busRoutesRepository.getDetailedRoute(routeId),
            busRoutesRepository.getStationSummaries(routeId).flatMapLatest { stations ->
                timeUpdate.map { now ->
                    stations.map { station ->
                        BusRouteDetails.Station(
                            slug = station.slug,
                            name = station.name,
                            nextDepartures = NextDepartures.Loaded(
                                getNextDepartures(
                                    stationSlug = station.slug,
                                    now = now,
                                )
                            ),
                            annotateAsNew = station.annotateAsNew
                        )
                    }
                }.onStart {
                    emit(stations.map { station ->
                        BusRouteDetails.Station(
                            slug = station.slug,
                            name = station.name,
                            nextDepartures = NextDepartures.Loading,
                            annotateAsNew = station.annotateAsNew
                        )
                    })
                }
            },
        ) { route, stations ->
            RouteState.Loaded(
                BusRouteDetails(
                    name = route.name,
                    additionalInfo = route.additionalInfo,
                    stations = stations,
                    operator = BusRouteDetails.Operator(
                        slug = route.operatorSlug,
                        name = route.operatorName,
                    ),
                    destination = BusRouteDetails.Destination(
                        slug = route.arrivalStationSlug,
                        //TODO: Read real value from database, once bus station map entities are there
                        name = when (route.arrivalStationSlug) {
                            "busbahnhof_ost" -> "Busbahnhof Ost"
                            "busbahnhof_nord" -> "Bustbahnhof Nord"
                            "busbahnhof_west" -> "Busbahnhof West"
                            "bushaltestelle_oldenburgerstr" -> "Oldenburgerstr."
                            else -> null
                        }
                    )
                )
            )
        }
            .map(RouteViewModel::ViewState)
            .stateIn(
                viewModelScope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState()
            )

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(
        val routeState: RouteState = RouteState.Loading,
    )

    sealed class RouteState {
        object Loading : RouteState()
        data class Loaded(val routeDetails: BusRouteDetails) : RouteState()
    }
}
