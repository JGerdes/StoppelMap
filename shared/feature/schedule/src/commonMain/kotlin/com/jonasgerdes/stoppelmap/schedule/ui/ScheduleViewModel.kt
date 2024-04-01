package com.jonasgerdes.stoppelmap.schedule.ui

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.data.Event
import com.jonasgerdes.stoppelmap.schedule.model.ScheduleDay
import com.jonasgerdes.stoppelmap.schedule.model.ScheduleEvent
import com.jonasgerdes.stoppelmap.schedule.model.ScheduleTime
import com.jonasgerdes.stoppelmap.schedule.repository.BookmarkedEventsRepository
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class ScheduleViewModel(
    private val eventRepository: EventRepository,
    private val bookmarkedEventsRepository: BookmarkedEventsRepository,
    private val clockProvider: ClockProvider,
) : KMMViewModel() {

    private val selectedDay = MutableStateFlow<LocalDate?>(clockProvider.nowAsLocalDateTime().date)
    private val selectedEvent = MutableStateFlow<Event?>(null)
    private val bookmarkedEvents = bookmarkedEventsRepository.getBookmarkedEvents()
    private val scheduleDays = bookmarkedEvents.map { notifications ->
        eventRepository.getAllEvents()
            .groupBy { it.start.date }
            .entries
            .map { (day, events) ->
                ScheduleDay(
                    day,
                    events.groupBy { it.start.time }
                        .entries
                        .map { (time, events) ->
                            ScheduleTime(
                                time,
                                events.sortedBy { it.location }
                                    .map { event ->
                                        ScheduleEvent(
                                            event = event,
                                            bookmarked = notifications.contains(
                                                event.slug
                                            )
                                        )

                                    }
                            )
                        }
                        .sortedBy { it.time }
                )
            }
            .sortedBy { it.date }
    }


    val state: StateFlow<ViewState> =
        combine(
            scheduleDays,
            selectedDay,
            selectedEvent
        ) { scheduleDays, selectedDay, selectedEvent ->
            ViewState(
                scheduleDays = scheduleDays,
                selectedDay = scheduleDays
                    .indexOfFirst { it.date == selectedDay }
                    .mapNegative1ToNull(),
                selectedEvent = selectedEvent
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
    )


    fun onDayTap(day: LocalDate?) {
        selectedDay.value = day
    }

    fun onEventTap(event: Event) {
        selectedEvent.update { current ->
            if (current == event) null else event
        }
    }

    fun onEventNotificationSchedule(event: Event, notificationActive: Boolean) {
        viewModelScope.coroutineScope.launch {
            if (notificationActive) {
                bookmarkedEventsRepository.addBookmarkedEvent(event.slug)
            } else {
                bookmarkedEventsRepository.removeBookmarkedEvent(event.slug)
            }
        }
    }
}

private fun Int.mapNegative1ToNull() = if (this == -1) null else this