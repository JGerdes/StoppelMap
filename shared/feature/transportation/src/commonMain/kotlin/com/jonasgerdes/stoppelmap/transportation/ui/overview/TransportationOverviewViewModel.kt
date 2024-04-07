@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jonasgerdes.stoppelmap.transportation.ui.overview

import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TaxiServiceRepository
import com.jonasgerdes.stoppelmap.transportation.data.TrainRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TransportationUserDataRepository
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import com.jonasgerdes.stoppelmap.transportation.model.TaxiService
import com.jonasgerdes.stoppelmap.transportation.usecase.GetNextDeparturesUseCase
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TransportationOverviewViewModel(
    busRoutesRepository: BusRoutesRepository,
    trainRoutesRepository: TrainRoutesRepository,
    taxiServiceRepository: TaxiServiceRepository,
    transportationUserDataRepository: TransportationUserDataRepository,
    private val getNextDepartures: GetNextDeparturesUseCase,
) : KMMViewModel() {

    private val timeUpdate = flow {
        while (true) {
            emit(Unit)
            delay(10_000)
        }
    }

    private val trainRoutesState = trainRoutesRepository.getAllRoutes()
        .map { TrainRoutesState(it) }

    private val busRoutesState =
        combine(
            busRoutesRepository.getAllRoutes(),
            transportationUserDataRepository.getFavouriteStations()
                .flatMapLatest { favs ->
                    timeUpdate.map {
                        favs.map { stationId ->
                            val (station, routeName, _) = busRoutesRepository.getStationById(
                                stationId
                            )
                                .first()
                            BusRouteDetails.Station.Stop(
                                id = station.id,
                                routeName = routeName,
                                title = station.title,
                                nextDepartures = getNextDepartures(station.departures.flatMap { it.departures })
                            )
                        }
                    }
                },
            TransportationOverviewViewModel::BusRoutesState
        )

    val state: StateFlow<ViewState> =
        combine(
            trainRoutesState,
            busRoutesState,
            flowOf(TaxiServicesState(taxiServiceRepository.getTaxiServices())),
            TransportationOverviewViewModel::ViewState
        )
            .stateIn(
                viewModelScope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState()
            )

    data class ViewState(
        val trainRoutesState: TrainRoutesState = TrainRoutesState(emptyList()),
        val busRoutesViewState: BusRoutesState = BusRoutesState(emptyList(), emptyList()),
        val taxiServicesState: TaxiServicesState = TaxiServicesState(emptyList()),
    )

    data class TrainRoutesState(val routes: List<RouteSummary>)
    data class BusRoutesState(
        val routes: List<RouteSummary>,
        val favouriteStations: List<BusRouteDetails.Station.Stop>
    )

    data class TaxiServicesState(val services: List<TaxiService>)
}
