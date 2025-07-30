@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jonasgerdes.stoppelmap.map.ui

import co.touchlab.kermit.Logger
import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.map.data.MapEntityRepository
import com.jonasgerdes.stoppelmap.map.location.LocationRepository
import com.jonasgerdes.stoppelmap.map.location.PermissionRepository
import com.jonasgerdes.stoppelmap.map.model.FullMapEntity
import com.jonasgerdes.stoppelmap.map.model.PermissionState
import com.jonasgerdes.stoppelmap.map.model.PermissionState.Granted
import com.jonasgerdes.stoppelmap.map.model.SearchResult
import com.jonasgerdes.stoppelmap.map.model.SensorLocation
import com.jonasgerdes.stoppelmap.map.model.StallPromotion
import com.jonasgerdes.stoppelmap.map.model.StallSummary
import com.jonasgerdes.stoppelmap.map.model.contains
import com.jonasgerdes.stoppelmap.map.model.reduceBoundingBox
import com.jonasgerdes.stoppelmap.map.model.toLocation
import com.jonasgerdes.stoppelmap.map.usecase.GetMapFilePathUseCase
import com.jonasgerdes.stoppelmap.map.usecase.GetQuickSearchSuggestionsUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchMapUseCase
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.Path
import kotlin.time.Duration.Companion.milliseconds

class MapViewModel(
    private val getMapFilePath: GetMapFilePathUseCase,
    private val searchMap: SearchMapUseCase,
    private val mapEntityRepository: MapEntityRepository,
    private val locationRepository: LocationRepository,
    private val permissionRepository: PermissionRepository,
    private val getQuickSearchItems: GetQuickSearchSuggestionsUseCase,
) : KMMViewModel() {

    private var searchJob: Job? = null
    private val searchState = MutableStateFlow(SearchState())
    private val bottomSheetState = MutableStateFlow<BottomSheetState>(BottomSheetState.Hidden)
    private val mapState = MutableStateFlow(MapState())
    private val ownLocationState = MutableStateFlow(OwnLocationState())

    private var switchToFollowOwnLocationPending = false

    init {
        Logger.d { "🗺️ MapViewModel init" }
        permissionRepository.getLocationPermissionState()
            .flatMapLatest { permissionState ->
                ownLocationState.update { it.copy(permissionState = permissionState) }
                Logger.d { "🛡️Permission State: $permissionState" }
                if (permissionState == Granted) {
                    locationRepository.getLocationUpdates()
                } else {
                    flowOf()
                }
            }
            .onEach { location ->
                Logger.d { "📍Location: $location" }
                if (switchToFollowOwnLocationPending) {
                    centerOnOwnLocation(location)
                }
                mapState.update { currentState ->
                    if (ownLocationState.value.isFollowingLocation && MapDefaults.cameraBounds.contains(location.toLocation())) {
                        currentState.copy(camera = CameraView.FocusLocation(location.toLocation()))
                    } else {
                        currentState
                    }.copy(ownLocation = location)
                }
            }
            .launchIn(viewModelScope.coroutineScope)

        getQuickSearchItems()
            .onEach { quickSearchChips ->
                searchState.update { it.copy(quickSearchChips = quickSearchChips) }
            }
            .launchIn(viewModelScope.coroutineScope)
    }

    val state: StateFlow<ViewState> =
        combine(
            getMapFilePath(),
            searchState,
            bottomSheetState,
            mapState,
            ownLocationState,
            ::ViewState
        )
            .stateIn(
                viewModelScope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState()
            )

    fun onSearch(query: String) {
        searchJob?.cancel()

        if (query.length < 2) {
            searchState.update { it.copy(results = emptyList(), inProgress = false) }
            return
        }
        searchState.update { it.copy(inProgress = true) }
        searchJob = viewModelScope.coroutineScope.launch {
            delay(300.milliseconds) // debounce user input
            val results = searchMap(query)
            searchState.update { it.copy(results = results, inProgress = false) }
        }
    }

    fun onMapTap(entitySlug: String?) {
        if (entitySlug == null) {
            clearSelectedEntity()
        } else {
            viewModelScope.coroutineScope.launch {
                showFullMapEntity(entitySlug, keepZoom = true)
            }
        }
    }

    fun onSearchResultTap(searchResult: SearchResult) {
        viewModelScope.coroutineScope.launch {
            searchJob?.cancel()
            searchState.update { it.copy(inProgress = false, results = emptyList()) }
            when (searchResult.type) {
                SearchResult.Type.SingleStall -> showFullMapEntity(searchResult.resultEntities.first().slug)
                SearchResult.Type.Collection -> showCollection(searchResult.term, searchResult.resultEntities)
            }
        }
    }

    fun onBottomSheetClose() {
        clearSelectedEntity()
    }

    fun requestLocationPermission() {
        permissionRepository.requestLocationPermission()
        switchToFollowOwnLocationPending = true
    }

    private fun clearSelectedEntity() {
        mapState.update { it.copy(highlightedEntities = null) }
        bottomSheetState.update { BottomSheetState.Hidden }
        //TODO: bottomSheetState.update { BottomSheetState.Idle() }
    }

    suspend fun showCollection(name: String, stalls: List<StallSummary>) {
        val geoData = mapEntityRepository.getGeoDataBySlugs(stalls.map { it.slug }.toSet())
        mapState.update {
            it.copy(
                camera = CameraView.Bounding(
                    bounds = geoData.values.reduceBoundingBox { boundingBox }
                ),
                highlightedEntities = stalls.mapNotNull { summary ->
                    geoData[summary.slug]?.center?.let { location ->
                        MapState.HighlightedEntity(
                            location = location,
                            name = summary.name,
                            icon = summary.icon,
                        )
                    }
                }
            )
        }
        bottomSheetState.update { BottomSheetState.Collection(name = name, stalls) }
    }

    private suspend fun showFullMapEntity(slug: String, keepZoom: Boolean = false) {
        val fullMapEntity = mapEntityRepository.getDetailedMapEntity(slug) ?: return
        mapState.update {
            it.copy(
                camera = if (keepZoom) CameraView.FocusLocation(fullMapEntity.location)
                else CameraView.Bounding(fullMapEntity.bounds),
                highlightedEntities = listOf(
                    MapState.HighlightedEntity(
                        location = fullMapEntity.location,
                        name = fullMapEntity.name,
                        icon = fullMapEntity.icon,
                    )
                )
            )
        }
        bottomSheetState.update { BottomSheetState.SingleStall(fullMapEntity) }
    }

    fun onCameraMoved() {
        switchToFollowOwnLocationPending = false
        ownLocationState.update { it.copy(isFollowingLocation = false) }
    }

    fun onCameraUpdateDispatched() {
        mapState.update { it.copy(camera = null) }
    }

    fun onLocationButtonTap() {
        centerOnOwnLocation(mapState.value.ownLocation)
    }

    private fun centerOnOwnLocation(location: SensorLocation?) {
        val currentLocation = location?.toLocation()
        switchToFollowOwnLocationPending = false
        ownLocationState.update {
            when {
                it.isFollowingLocation || currentLocation == null -> it.copy(isFollowingLocation = false)
                MapDefaults.cameraBounds.contains(currentLocation) -> {
                    mapState.update { it.copy(camera = CameraView.FocusLocation(currentLocation)) }
                    it.copy(isFollowingLocation = true)
                }

                else -> it.copy(showNotInAreaHint = true)
            }
        }
    }

    fun onNotInAreaHintShow() {
        ownLocationState.update { it.copy(showNotInAreaHint = false) }
    }

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(
        val mapDataPath: Path? = null,
        val searchState: SearchState = SearchState(),
        val bottomSheetState: BottomSheetState = BottomSheetState.Hidden,
        val mapState: MapState = MapState(),
        val locationState: OwnLocationState = OwnLocationState(),
    )

    data class SearchState(
        val results: List<SearchResult> = emptyList(),
        val history: List<SearchResult> = emptyList(),
        val quickSearchChips: List<SearchResult> = emptyList(),
        val inProgress: Boolean = false,
    )

    sealed interface BottomSheetState {
        data object Hidden : BottomSheetState
        data class Idle(
            val favourites: List<StallSummary> = emptyList(),
            val latestSearchResults: List<com.jonasgerdes.stoppelmap.map.model.SearchResult> = emptyList(),
            val promotions: List<StallPromotion> = emptyList()
        ) : BottomSheetState

        data class SingleStall(val fullMapEntity: FullMapEntity) : BottomSheetState

        data class Collection(val name: String, val stalls: List<StallSummary>) : BottomSheetState {
            fun subline() = "${stalls.size} mal auf dem Stoppelmarkt" //TODO: localize
        }
    }

    data class OwnLocationState(
        val permissionState: PermissionState = PermissionState.NotDetermined,
        val showNotInAreaHint: Boolean = false,
        val isFollowingLocation: Boolean = false,
    )
}