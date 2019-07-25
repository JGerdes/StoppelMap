package com.jonasgerdes.stoppelmap.preperation

import com.jonasgerdes.stoppelmap.preperation.entity.*
import java.sql.Connection

data class Data(
    val stalls: MutableList<Stall> = mutableListOf(),
    val images: MutableList<Image> = mutableListOf(),
    val alias: MutableList<Alias> = mutableListOf(),
    val urls: MutableList<Url> = mutableListOf(),
    val phones: MutableList<Phone> = mutableListOf(),
    val restrooms: MutableList<Restroom> = mutableListOf(),
    val items: MutableMap<String, List<Item>> = mutableMapOf(),
    val stallItems: MutableList<StallItem> = mutableListOf(),
    val subTypes: MutableList<SubType> = mutableListOf(),
    val stallSubTypes: MutableList<StallSubType> = mutableListOf(),
    val events: MutableList<Event> = mutableListOf(),
    val tags: MutableMap<String, Tag> = mutableMapOf(),
    val eventTags: MutableList<EventTag> = mutableListOf(),
    val artists: MutableMap<String, Artist> = mutableMapOf(),
    val eventArtists: MutableList<EventArtist> = mutableListOf(),

    val routes: MutableList<Route> = mutableListOf(),
    val stations: MutableList<Station> = mutableListOf(),
    val departures: MutableList<Departure> = mutableListOf(),
    val transportPrices: MutableList<TransportPrice> = mutableListOf()
) {

    fun insertInto(db: Connection) {
        stalls.apply { if (isNotEmpty()) db.insert(this) }
        images.apply { if (isNotEmpty()) db.insert(this) }
        alias.apply { if (isNotEmpty()) db.insert(this) }
        urls.apply { if (isNotEmpty()) db.insert(this) }
        phones.apply { if (isNotEmpty()) db.insert(this) }
        restrooms.apply { if (isNotEmpty()) db.insert(this) }
        items.values.toList().apply { if (isNotEmpty()) db.insert(this) }
        stallItems.apply { if (isNotEmpty()) db.insert(this) }
        subTypes.apply { if (isNotEmpty()) db.insert(this) }
        stallSubTypes.apply { if (isNotEmpty()) db.insert(this) }
        events.apply { if (isNotEmpty()) db.insert(this) }
        tags.values.toList().apply { if (isNotEmpty()) db.insert(this) }
        eventTags.apply { if (isNotEmpty()) db.insert(this) }
        artists.values.toList().apply { if (isNotEmpty()) db.insert(this) }
        eventArtists.apply { if (isNotEmpty()) db.insert(this) }

        routes.apply { if (isNotEmpty()) db.insert(this) }
        stations.apply { if (isNotEmpty()) db.insert(this) }
        departures.apply {
            val unique = this.distinct()
            System.err.println("original size: $size, unique size: ${unique.size}")
            val duplicates = subtract(unique)
            System.err.println("duplicates size: ${duplicates.size}")
            if (duplicates.isNotEmpty()) {
                System.err.println("Found ${duplicates.size} duplicated departures, this shouldn't happen. Ignoring:")
                duplicates.forEach {
                    System.err.println("\t$it")
                }
            }
            if (unique.isNotEmpty()) db.insert(unique)
        }
        transportPrices.apply { if (isNotEmpty()) db.insert(this) }
    }
}
