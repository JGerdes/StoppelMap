package com.jonasgerdes.stoppelmap.map.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.map.entity.SearchResult
import com.jonasgerdes.stoppelmap.map.usecase.SearchForStallsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SEARCH_DEBOUNCE_DELAY_MS = 100L

class MapViewModel(
    private val searchForStalls: SearchForStallsUseCase
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> get() = _searchResults

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
}