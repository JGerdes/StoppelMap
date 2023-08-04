@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jonasgerdes.stoppelmap.transportation.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TrainRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TransportationUserDataRepository
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import com.jonasgerdes.stoppelmap.transportation.usecase.GetNextDeparturesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class TransportationOverviewViewModel(
    busRoutesRepository: BusRoutesRepository,
    trainRoutesRepository: TrainRoutesRepository,
    transportationUserDataRepository: TransportationUserDataRepository,
    private val getNextDepartures: GetNextDeparturesUseCase,
) : ViewModel() {

    private val timeUpdate = flow {
        while (true) {
            emit(Unit)
            delay(10_000)
        }
    }

    private val trainRoutesState = trainRoutesRepository.getAllRoutes()
        .map { TrainRoutesState(it) }

    private val busRoutesState = busRoutesRepository.getAllRoutes()
        .map { BusRoutesState(it) }


    private val favouriteState = transportationUserDataRepository.getFavouriteStations()
        .flatMapLatest { favs ->
            timeUpdate.map {
                FavouriteState.Loaded(
                    favs.map { stationId ->
                        val station = busRoutesRepository.getStationById(stationId).first()
                        BusRouteDetails.Station.Stop(
                            id = station.id,
                            title = station.title,
                            nextDepartures = getNextDepartures(station.departures.flatMap { it.departures })
                        )
                    }
                ) as FavouriteState
            }
        }.onStart { emit(FavouriteState.Loading) }

    val state: StateFlow<ViewState> =
        combine(
            trainRoutesState,
            busRoutesState,
            favouriteState,
            ::ViewState
        )
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState()
            )

    data class ViewState(
        val trainRoutesState: TrainRoutesState = TrainRoutesState(emptyList()),
        val busRoutesViewState: BusRoutesState = BusRoutesState(emptyList()),
        val favouriteState: FavouriteState = FavouriteState.Loading
    )

    data class TrainRoutesState(val routes: List<RouteSummary>)
    data class BusRoutesState(val routes: List<RouteSummary>)

    sealed interface FavouriteState {
        object Loading : FavouriteState
        data class Loaded(val favouriteStations: List<BusRouteDetails.Station.Stop>) :
            FavouriteState
    }
}
