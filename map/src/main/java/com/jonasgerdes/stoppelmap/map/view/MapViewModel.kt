package com.jonasgerdes.stoppelmap.map.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.map.entity.SearchResult
import com.jonasgerdes.stoppelmap.map.usecase.SearchForStallsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(
    private val searchForStalls: SearchForStallsUseCase
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> get() = _searchResults

    fun onSearchEntered(text: String) {
        if (text.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
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