package com.jonasgerdes.stoppelmap.schedule.ui

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.schedule.model.BookmarkedEvents
import com.jonasgerdes.stoppelmap.schedule.model.Event
import com.jonasgerdes.stoppelmap.schedule.model.ScheduleDay
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import com.jonasgerdes.stoppelmap.schedule.usecase.GetBookmarkedEventsUseCase
import com.jonasgerdes.stoppelmap.schedule.usecase.GetScheduleDaysUseCase
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class ScheduleViewModel(
    getScheduleDays: GetScheduleDaysUseCase,
    private val eventRepository: EventRepository,
    private val clockProvider: ClockProvider,
    private val getBookmarkedEvents: GetBookmarkedEventsUseCase
) : KMMViewModel() {

    private val selectedDay = MutableStateFlow<LocalDate?>(clockProvider.nowAsLocalDateTime().date)
    private val selectedEvent = MutableStateFlow<Event?>(null)

    val state: StateFlow<ViewState> =
        combine(
            getScheduleDays(),
            selectedDay,
            selectedEvent,
            getBookmarkedEvents(),
        ) { scheduleDays, selectedDay, selectedEvent, bookmarkedEvents ->
            ViewState(
                scheduleDays = scheduleDays,
                selectedDay = scheduleDays
                    .indexOfFirst { it.date == selectedDay }
                    .mapNegative1ToNull(),
                selectedEvent = selectedEvent,
                bookmarkedEvents = bookmarkedEvents,
            )
        }
            .stateIn(
                viewModelScope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState()
            )

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(
        val scheduleDays: List<ScheduleDay> = emptyList(),
        val selectedDay: Int? = null,
        val selectedEvent: Event? = null,
        val bookmarkedEvents: BookmarkedEvents = BookmarkedEvents.None
    )


    fun onDayTap(day: LocalDate?) {
        selectedDay.value = day
    }

    fun onEventTap(event: Event) {
        selectedEvent.update { current ->
            if (current == event) null else event
        }
    }

    fun onEventNotificationSchedule(eventSlug: String, notificationActive: Boolean) {
        viewModelScope.coroutineScope.launch {
            if (notificationActive) {
                eventRepository.addBookmark(eventSlug)
            } else {
                eventRepository.removeBookmark(eventSlug)
            }
        }
    }
}

private fun Int.mapNegative1ToNull() = if (this == -1) null else this

