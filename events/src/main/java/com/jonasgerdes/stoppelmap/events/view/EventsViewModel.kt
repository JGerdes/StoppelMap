package com.jonasgerdes.stoppelmap.events.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.events.entity.Day
import com.jonasgerdes.stoppelmap.events.entity.Event
import com.jonasgerdes.stoppelmap.events.usecase.GetEventsByDayUseCase
import com.jonasgerdes.stoppelmap.events.usecase.GetStallBySlugUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

private typealias ModelEvent = com.jonasgerdes.stoppelmap.model.events.Event

class EventsViewModel @Inject constructor(
    private val getEventsByDay: GetEventsByDayUseCase,
    private val getStallBySlug: GetStallBySlugUseCase
) : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    private var dayJob: Job? = null

    fun setDay(day: Day) {
        dayJob?.cancel()
        dayJob = viewModelScope.launch {
            val events = getEventsByDay(day).map { event ->
                Event(
                    title = event.name,
                    location = event.location?.let { getStallBySlug(it) },
                    description = event.description,
                    isFinished = false,
                    start = event.start
                )
            }
            _events.postValue(events)
        }
    }
}