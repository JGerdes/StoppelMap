package com.jonasgerdes.stoppelmap.schedule.ui

import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.schedule.model.ScheduleDay
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class ScheduleViewModel(
    private val eventRepository: EventRepository,
    private val getCurrentLocalDateTime: () -> LocalDateTime,
) : ViewModel() {

    private val scheduleState = MutableStateFlow(ScheduleState())

    init {
        viewModelScope.launch {
            val today = getCurrentLocalDateTime().date
            val scheduleDays = eventRepository.getAllEvents()
                .map {
                    it.copy(
                        description = it.description?.removeHtml()
                    )
                }
                .groupBy { it.start.date }
                .entries
                .map { (day, events) -> ScheduleDay(day, events.sortedBy { it.start }) }
                .sortedBy { it.date }
            scheduleState.value = ScheduleState(
                scheduleDays = scheduleDays,
                selectedDay = scheduleDays.indexOfFirst { it.date == today }.mapNegative1ToNull()
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
        val scheduleDays: List<ScheduleDay> = emptyList(),
        val selectedDay: Int? = null
    )

    fun onDayTap(day: ScheduleDay) {
        scheduleState.value = scheduleState.value.let { state ->
            state.copy(
                selectedDay = state.scheduleDays.indexOf(day).mapNegative1ToNull()
            )
        }
    }
}


private fun String.removeHtml() =
    HtmlCompat.fromHtml(
        this,
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString().trim()


private fun Int.mapNegative1ToNull() = if (this == -1) null else this
