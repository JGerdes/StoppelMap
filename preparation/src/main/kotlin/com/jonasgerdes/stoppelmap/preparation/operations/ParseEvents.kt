package com.jonasgerdes.stoppelmap.preparation.operations

import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import com.jonasgerdes.stoppelmap.preparation.Data
import com.jonasgerdes.stoppelmap.preperation.asSlug
import com.jonasgerdes.stoppelmap.preperation.emptyIfNull
import com.jonasgerdes.stoppelmap.preperation.entity.Artist
import com.jonasgerdes.stoppelmap.preperation.entity.Event
import com.jonasgerdes.stoppelmap.preperation.entity.EventArtist
import com.jonasgerdes.stoppelmap.preperation.entity.EventTag
import com.jonasgerdes.stoppelmap.preperation.entity.JsonEventWrapper
import com.jonasgerdes.stoppelmap.preperation.entity.Tag
import com.jonasgerdes.stoppelmap.preperation.entity.Url
import kotlinx.datetime.toKotlinLocalDateTime
import org.jsoup.Jsoup
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private val eventJsonFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

fun Data.parseEventSchedule(vararg files: File) {


    System.out.println("Reading events from ${files.size} files")

    val gson = GsonBuilder().create()
    files.map { JsonReader(it.reader()) }
        .map { gson.fromJson<JsonEventWrapper>(it, JsonEventWrapper::class.java) }
        .flatMap { it.events }
        .filter {
            !it.uuid.isBlank().apply {
                if (this) {
                    System.err.println("event without uuid: $it")
                }
            }
        }
        .forEach {
            val event = Event(
                slug = it.uuid,
                name = it.name,
                start = LocalDateTime.parse(it.start, eventJsonFormat).toKotlinLocalDateTime(),
                end = it.end?.let {
                    LocalDateTime.parse(it, eventJsonFormat).toKotlinLocalDateTime()
                },
                description = it.description?.also { println("before: $it") }
                    ?.replace("<li>", "<li>\u2022 ")
                    ?.let {
                        Jsoup.parse(it)
                            .wholeText()
                            .lines()
                            .filter {
                                it.isNotBlank()
                            }
                            .joinToString(separator = "\n")
                    }
                    ?.trim()
                    ?.ifBlank { null }
                    ?.also { println("after: $it") },
                location = it.locationUuid,
                locationName = it.locationName,
                isOfficial = it.isOfficial
            )
            eventTags += it.tags.emptyIfNull()
                .filter { !it.isBlank() }
                .map { Tag(it.asSlug(), it) }
                .map {
                    if (!tags.containsKey(it.slug)) {
                        tags[it.slug] = it
                    }
                    tags[it.slug]!!
                }.map {
                    EventTag(
                        event = event.slug,
                        tag = it.slug
                    )
                }

            eventArtists += it.artists.emptyIfNull()
                .filter { !it.isBlank() }
                .map { Artist(it.asSlug(), it) }
                .map {
                    if (!artists.containsKey(it.slug)) {
                        artists[it.slug] = it
                    }
                    artists[it.slug]!!
                }.map {
                    EventArtist(
                        event = event.slug,
                        artist = it.slug
                    )
                }

            it.facebookUrl?.let {
                if (!it.isBlank()) {
                    urls += Url(
                        event = event.slug,
                        url = it,
                        type = "facebook"
                    )
                }
            }

            events.add(event)
        }
}
