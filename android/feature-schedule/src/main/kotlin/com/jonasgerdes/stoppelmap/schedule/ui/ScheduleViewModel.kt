package com.jonasgerdes.stoppelmap.schedule.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.data.Event
import com.jonasgerdes.stoppelmap.schedule.model.ScheduleDay
import com.jonasgerdes.stoppelmap.schedule.model.ScheduleEvent
import com.jonasgerdes.stoppelmap.schedule.model.ScheduleTime
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

class ScheduleViewModel(
    private val eventRepository: EventRepository,
    private val clockProvider: ClockProvider,
) : ViewModel() {

    private val selectedDay = MutableStateFlow<LocalDate?>(clockProvider.nowAsLocalDateTime().date)
    private val selectedEvent = MutableStateFlow<Event?>(null)
    private val notifications = MutableStateFlow(emptySet<String>())
    private val scheduleDays = notifications.map { notifications ->
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
                                            notificationActive = notifications.contains(
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
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState()
            )

    data class ViewState(
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
        notifications.update { current ->
            if (notificationActive) {
                current + event.slug
            } else {
                current - event.slug
            }
        }
    }
}


private fun Int.mapNegative1ToNull() = if (this == -1) null else this
