package com.jonasgerdes.stoppelmap.schedule.ui

import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.schedule.model.ScheduleDay
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val scheduleState = MutableStateFlow(ScheduleState())

    init {
        viewModelScope.launch {
            scheduleState.value = ScheduleState(
                scheduleDays = eventRepository.getAllEvents()
                    .map {
                        it.copy(
                            description = it.description?.removeHtml()
                        )
                    }
                    .groupBy { it.start.dayOfWeek }
                    .entries
                    .map { (day, events) -> ScheduleDay(day, events.sortedBy { it.start }) }
            )
        }
    }

    val state: StateFlow<ViewState> =
        scheduleState
            .map(::ViewState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState()
            )

    data class ViewState(
        val scheduleState: ScheduleState = ScheduleState(),
    )

    data class ScheduleState(
        val scheduleDays: List<ScheduleDay> = emptyList()
    )
}


private fun String.removeHtml() =
    HtmlCompat.fromHtml(
        this,
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString().trim()
