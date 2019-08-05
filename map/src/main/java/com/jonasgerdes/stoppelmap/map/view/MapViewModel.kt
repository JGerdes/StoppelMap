package com.jonasgerdes.stoppelmap.map.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.androidutil.withDefault
import com.jonasgerdes.stoppelmap.map.entity.Highlight
import com.jonasgerdes.stoppelmap.map.entity.Location
import com.jonasgerdes.stoppelmap.map.entity.MapFocus
import com.jonasgerdes.stoppelmap.map.entity.SearchResult
import com.jonasgerdes.stoppelmap.map.usecase.GetFullStallsBySlugUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchForStallsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SEARCH_DEBOUNCE_DELAY_MS = 100L

class MapViewModel(
    private val searchForStalls: SearchForStallsUseCase,
    private val getStallsBySlug: GetFullStallsBySlugUseCase
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> get() = _searchResults

    private val _mapFocus = MutableLiveData<MapFocus>().withDefault(MapFocus.None)
    val mapFocus: LiveData<MapFocus> get() = _mapFocus

    private val _highlightedStalls = MutableLiveData<List<Highlight>>().withDefault(emptyList())
    val highlightedStalls: LiveData<List<Highlight>> get() = _highlightedStalls

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

    fun onSearchResultSelected(stallsSlugs: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val stalls = getStallsBySlug(stallsSlugs)
            _mapFocus.postValue(MapFocus.All(stalls.flatMap { stall ->
                listOf(
                    Location(longitude = stall.minLng, latitude = stall.minLat),
                    Location(longitude = stall.maxLng, latitude = stall.maxLat)
                )
            }))
            _highlightedStalls.postValue(stalls.map {
                Highlight.SingleStall(stall = it)
            })
        }
    }

    fun onStallHighlightedSelected(highlight: Highlight) {
        val stalls = when (highlight) {
            is Highlight.SingleStall -> listOf(highlight.stall)
            is Highlight.Stalls -> highlight.stalls
        }
        _mapFocus.postValue(MapFocus.All(stalls.flatMap { stall ->
            listOf(
                Location(latitude = stall.minLat, longitude = stall.minLng),
                Location(latitude = stall.maxLat, longitude = stall.maxLng)
            )
        }))
    }

    fun onStallClicked(slug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val stall = getStallsBySlug(listOf(slug)).first()
            _mapFocus.postValue(
                MapFocus.All(
                    listOf(
                        Location(longitude = stall.minLng, latitude = stall.minLat),
                        Location(longitude = stall.maxLng, latitude = stall.maxLat)
                    )
                )
            )
            _highlightedStalls.postValue(listOf(Highlight.SingleStall(stall = stall)))
        }
    }
}