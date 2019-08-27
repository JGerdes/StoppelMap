package com.jonasgerdes.stoppelmap.map.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.androidutil.withDefault
import com.jonasgerdes.stoppelmap.core.routing.Route.Map.State.Carousel.StallCollection
import com.jonasgerdes.stoppelmap.map.entity.*
import com.jonasgerdes.stoppelmap.map.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SEARCH_DEBOUNCE_DELAY_MS = 100L

class MapViewModel(
    private val searchForStalls: SearchForStallsUseCase,
    private val getStallsBySlug: GetFullStallsBySlugUseCase,
    private val createSingleStallHighlight: CreateSingleStallHighlightUseCase,
    private val createTypeHighlights: CreateTypeHighlightsUseCase,
    private val createItemHighlights: CreateItemHighlightsUseCase,
    private val getUserLocation: GetUserLocationUseCase,
    private val isUserInArea: IsUserInAreaUseCase
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> get() = _searchResults

    private val _mapFocus = MutableLiveData<MapFocus>().withDefault(MapFocus.None)
    val mapFocus: LiveData<MapFocus> get() = _mapFocus

    private val _highlightedStalls = MutableLiveData<List<Highlight>>().withDefault(emptyList())
    val highlightedStalls: LiveData<List<Highlight>> get() = _highlightedStalls

    private val _message = MutableLiveData<Message>()
    val message: LiveData<Message> get() = _message

    private var searchJob: Job? = null

    fun onSearchEntered(text: String) {
        if (text.isNotEmpty()) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch(Dispatchers.IO) {
                delay(SEARCH_DEBOUNCE_DELAY_MS)
                val searchResults = searchForStalls(text)
                Log.d("MapViewModel", "Found stalls:")
                searchResults.forEach {
                    Log.d("MapViewModel", "${it.title}")
                }
                _searchResults.postValue(searchResults)
            }
        } else {
            //TODO: show empty state with requests from history etc
            _searchResults.postValue(emptyList())
        }
    }

    fun onStallsSelected(stalls: StallCollection) {
        viewModelScope.launch(Dispatchers.IO) {

            val highlights = when (stalls) {
                is StallCollection.Single -> listOf(createSingleStallHighlight(stalls.stallSlug))
                is StallCollection.TypeCollection -> createTypeHighlights(stalls.stallSlugs, stalls.type)
                is StallCollection.ItemCollection -> createItemHighlights(stalls.stallSlugs, stalls.item)
            }

            if (highlights.isNotEmpty()) {
                onStallHighlightedSelected(highlights.first())
            }

            _highlightedStalls.postValue(highlights)
        }
    }

    fun onStallHighlightedSelected(highlight: Highlight) {
        val mapFocus = when (highlight) {
            is Highlight.SingleStall -> MapFocus.One(
                Location(
                    latitude = highlight.stall.basicInfo.centerLat,
                    longitude = highlight.stall.basicInfo.centerLng
                )
            )
            is Highlight.NamelessStall -> MapFocus.One(
                Location(
                    latitude = highlight.stall.basicInfo.centerLat,
                    longitude = highlight.stall.basicInfo.centerLng
                )
            )
            else -> {
                val coordinates = highlight.getStalls()
                    .map { it.basicInfo }
                    .flatMap { stall ->
                        listOf(
                            Location(latitude = stall.minLat, longitude = stall.minLng),
                            Location(latitude = stall.maxLat, longitude = stall.maxLng)
                        )
                    }
                MapFocus.All(coordinates)
            }
        }

        _mapFocus.postValue(mapFocus)
    }

    fun StallCollection.getStallSlugs() = when (this) {
        is StallCollection.Single -> listOf(stallSlug)
        is StallCollection.TypeCollection -> stallSlugs
        is StallCollection.ItemCollection -> stallSlugs
    }

    fun onHighlightsHidden() {
        _mapFocus.postValue(MapFocus.None)
        _highlightedStalls.postValue(emptyList())
    }

    fun onCenterOnUserTriggered() {
        viewModelScope.launch {
            val userLocation = getUserLocation()
            when (isUserInArea(userLocation)) {
                IsUserInAreaUseCase.UserState.IN_AREA ->
                    _mapFocus.postValue(
                        MapFocus.One(
                            Location(
                                latitude = userLocation!!.latitude,
                                longitude = userLocation.longitude
                            )
                        )
                    )
                IsUserInAreaUseCase.UserState.OUTSIDE_AREA -> _message.postValue(Message.NotInArea)
                IsUserInAreaUseCase.UserState.UNDEFINED -> {
                    //do nothing
                }
            }
        }
    }
}