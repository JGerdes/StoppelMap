package com.jonasgerdes.stoppelmap.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jonasgerdes.stoppelmap.model.events.Event
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

private val LOCAL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")

@Dao
abstract class EventDao {
    @Query("SELECT * FROM events ORDER BY start ASC, name ASC")
    suspend abstract fun getAllEvents(): List<Event>

    @Query("SELECT * FROM events where date(start) is date(:date) ORDER BY start ASC, name ASC")
    suspend abstract fun getAllEventsAt(date: String): List<Event>

    suspend fun getAllEventsAt(date: LocalDate) = getAllEventsAt(date.format(LOCAL_DATE_FORMAT))
}