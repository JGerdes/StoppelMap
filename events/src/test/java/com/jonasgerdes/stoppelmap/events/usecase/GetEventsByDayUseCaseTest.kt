package com.jonasgerdes.stoppelmap.events.usecase

import com.jonasgerdes.stoppelmap.core.domain.GlobalInfoProvider
import com.jonasgerdes.stoppelmap.data.DEFAULT_EVENT
import com.jonasgerdes.stoppelmap.data.StoppelmapDatabaseStub
import com.jonasgerdes.stoppelmap.data.repository.EventRepository
import com.jonasgerdes.stoppelmap.events.entity.Day
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.threeten.bp.*


class GetEventsByDayUseCaseTest {

    private val database = StoppelmapDatabaseStub()
    private val eventRepository = EventRepository(database)
    private val infoProvider = mock<GlobalInfoProvider> {
        on { getCurrentSeason() } doReturn GlobalInfoProvider.Season(
            title = "Stoppelmarkt 2019",
            start = LocalDateTime.of(
                2019,Month.AUGUST,15,18,0,0
            ).atStoppelmarktOffset(),
            days = (15..20).map { LocalDate.of(2019, Month.AUGUST, it) }
        )
    }
    val thursday = LocalDate.of(2019, Month.AUGUST, 15)
    val friday = LocalDate.of(2019, Month.AUGUST, 16)

    @Test
    fun `all events on day are found`() = runBlocking {
        val getEventsByDay = GetEventsByDayUseCase(eventRepository, infoProvider)

        // given
        database.with(
            DEFAULT_EVENT.copy(slug = "happy-hour", start = dateTimeOf(friday, 14, 0)),
            DEFAULT_EVENT.copy(
                slug = "senioren-nachmittag",
                start = dateTimeOf(friday, 15, 0)
            )
        )

        // when
        val result = getEventsByDay(Day(1))

        // then
        assertEquals(2, result.size)
    }

    @Test
    fun `only events of specified day are found`() = runBlocking {
        val getEventsByDay = GetEventsByDayUseCase(eventRepository, infoProvider)

        // given
        database.with(
            DEFAULT_EVENT.copy(slug = "opening", start = dateTimeOf(thursday, 18, 0)),
            DEFAULT_EVENT.copy(slug = "happy-hour", start = dateTimeOf(friday, 14, 0))
        )

        // when
        val result = getEventsByDay(Day(1))

        // then
        assertEquals(1, result.size)
    }
}

fun dateTimeOf(date: LocalDate, hour: Int, minute: Int) =
    LocalDateTime.of(date, LocalTime.of(hour, minute, 0)).atStoppelmarktOffset()