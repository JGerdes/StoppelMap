package com.jonasgerdes.stoppelmap.model

import com.jonasgerdes.stoppelmap.model.entity.*
import java.sql.Connection

data class Data(
        val stalls: MutableList<Stall> = mutableListOf(),
        val images: MutableList<Image> = mutableListOf(),
        val alias: MutableList<Alias> = mutableListOf(),
        val urls: MutableList<Url> = mutableListOf(),
        val phones: MutableList<Phone> = mutableListOf(),
        val restrooms: MutableList<Restroom> = mutableListOf(),
        val items: MutableMap<String, Item> = mutableMapOf(),
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
        db.insert(stalls)
        db.insert(images)
        db.insert(alias)
        db.insert(urls)
        db.insert(phones)
        db.insert(restrooms)
        db.insert(items.values.toList())
        db.insert(stallItems)
        db.insert(subTypes)
        db.insert(stallSubTypes)
        db.insert(events)
        db.insert(tags.values.toList())
        db.insert(eventTags)
        db.insert(artists.values.toList())
        db.insert(eventArtists)

        db.insert(routes)
        db.insert(stations)
        db.insert(departures)
        db.insert(transportPrices)
    }
}
