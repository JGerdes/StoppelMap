package com.jonasgerdes.stoppelmap.map.ui

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.map.data.MapEntityRepository
import com.jonasgerdes.stoppelmap.map.model.FullMapEntity
import com.jonasgerdes.stoppelmap.map.model.SearchResult
import com.jonasgerdes.stoppelmap.map.model.StallPromotion
import com.jonasgerdes.stoppelmap.map.model.StallSummary
import com.jonasgerdes.stoppelmap.map.usecase.GetMapFilePathUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchMapUseCase
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.Path
import kotlin.time.Duration.Companion.milliseconds

class MapViewModel(
    private val getMapFilePath: GetMapFilePathUseCase,
    private val searchMap: SearchMapUseCase,
    private val mapEntityRepository: MapEntityRepository,
) : KMMViewModel() {

    private var searchJob: Job? = null
    private val searchState = MutableStateFlow(SearchState())
    private val bottomSheetState = MutableStateFlow<BottomSheetState>(BottomSheetState.Hidden)
    private val mapState = MutableStateFlow(MapState())

    val state: StateFlow<ViewState> =
        combine(
            getMapFilePath(),
            searchState,
            bottomSheetState,
            mapState,
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
            when (searchResult.type) {
                SearchResult.Type.SingleStall -> showFullMapEntity(searchResult.resultEntities.first().slug)
            }
        }
    }

    fun onBottomSheetClose() {
        clearSelectedEntity()
    }

    fun clearSelectedEntity() {
        mapState.update { it.copy(highlightedEntities = null) }
        bottomSheetState.update { BottomSheetState.Hidden }
        //TODO: bottomSheetState.update { BottomSheetState.Idle() }
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

    }

    fun onCameraUpdateDispatched() {
        mapState.update { it.copy(camera = null) }
    }

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(
        val mapDataPath: Path? = null,
        val searchState: SearchState = SearchState(),
        val bottomSheetState: BottomSheetState = BottomSheetState.Hidden,
        val mapState: MapState = MapState()
    )

    data class SearchState(
        val results: List<SearchResult> = emptyList(),
        val history: List<SearchResult> = emptyList(),
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

        data class SearchResult(val searchResult: com.jonasgerdes.stoppelmap.map.model.SearchResult) : BottomSheetState
    }
}