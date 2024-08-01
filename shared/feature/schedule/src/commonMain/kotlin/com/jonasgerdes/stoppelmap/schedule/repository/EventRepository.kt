package com.jonasgerdes.stoppelmap.schedule.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jonasgerdes.stoppelmap.data.schedule.EventQueries
import com.jonasgerdes.stoppelmap.data.shared.Localized_stringQueries
import com.jonasgerdes.stoppelmap.data.shared.getLocalesForKeys
import com.jonasgerdes.stoppelmap.schedule.model.Event
import com.jonasgerdes.stoppelmap.schedule.model.EventSlug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

class EventRepository(
    private val eventQueries: EventQueries,
    private val localizedStringQueries: Localized_stringQueries,
    private val dataStore: DataStore<Preferences>,
) {

    private val bookmarkedEventKey = stringSetPreferencesKey("bookmarkedEvent")

    private fun getBookmarkedEventsSlugs() =
        dataStore.data.map { it[bookmarkedEventKey] ?: emptySet() }

    suspend fun addBookmark(eventSlug: EventSlug) {
        dataStore.edit {
            it[bookmarkedEventKey] = (it[bookmarkedEventKey] ?: emptySet()) + eventSlug
        }
    }

    suspend fun removeBookmark(eventSlug: EventSlug) {
        dataStore.edit {
            it[bookmarkedEventKey] = (it[bookmarkedEventKey] ?: emptySet()) - eventSlug
        }
    }

    fun getAllEvents() = combine(
        getBookmarkedEventsSlugs(),
        eventQueries.getAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
    ) { bookmarkedEventSlugs, events ->
        val nameStrings =
            localizedStringQueries
                .getLocalesForKeys(events.map { it.nameKey })

        val descriptionStrings =
            localizedStringQueries
                .getLocalesForKeys(events.mapNotNull { it.descriptionKey })
        events.map {
            Event(
                slug = it.slug,
                name = nameStrings[it.nameKey] ?: mapOf("de" to it.nameKey), //TODO: Improve this
                start = it.start,
                end = it.end,
                locationSlug = it.locationSlug,
                locationName = it.locationName,
                description = descriptionStrings[it.descriptionKey],
                isBookmarked = bookmarkedEventSlugs.contains(it.slug)
            )
        }
    }

    fun getBookmarkedEvents(after: LocalDateTime): Flow<List<Event>> =
        combine(
            getBookmarkedEventsSlugs(),
            eventQueries.getAllAfter(after)
                .asFlow()
                .mapToList(Dispatchers.IO)
        ) { bookmarkedEventSlugs, events ->
            val bookmarkedEvents = events.filter { bookmarkedEventSlugs.contains(it.slug) }
            val nameStrings =
                localizedStringQueries
                    .getLocalesForKeys(events.map { it.nameKey })
            val descriptionStrings =
                localizedStringQueries.getLocalesForKeys(
                    bookmarkedEvents.mapNotNull { it.descriptionKey }
                )
            bookmarkedEvents.map {
                Event(
                    slug = it.slug,
                    name = nameStrings[it.nameKey] ?: mapOf("de" to it.nameKey), //TODO: Improve this
                    start = it.start,
                    end = it.end,
                    locationSlug = it.locationSlug,
                    locationName = it.locationName,
                    description = descriptionStrings[it.descriptionKey],
                    isBookmarked = true,
                )
            }
        }

    fun getAllOfficialEvents(after: LocalDateTime): Flow<List<Event>> =
        combine(
            getBookmarkedEventsSlugs(),
            eventQueries.getAllOfficialAfter(after)
                .asFlow()
                .mapToList(Dispatchers.IO)
        ) { bookmarkedEventSlugs, events ->
            val nameStrings =
                localizedStringQueries
                    .getLocalesForKeys(events.map { it.nameKey })
            val descriptionStrings =
                localizedStringQueries
                    .getLocalesForKeys(events.mapNotNull { it.descriptionKey })
            events.map {
                Event(
                    slug = it.slug,
                    name = nameStrings[it.nameKey] ?: mapOf("de" to it.nameKey), //TODO: Improve this
                    start = it.start,
                    end = it.end,
                    locationSlug = it.locationSlug,
                    locationName = it.locationName,
                    description = descriptionStrings[it.descriptionKey],
                    isBookmarked = bookmarkedEventSlugs.contains(it.slug)
                )
            }
        }
}
