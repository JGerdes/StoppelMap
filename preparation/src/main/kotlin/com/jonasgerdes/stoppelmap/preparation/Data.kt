package com.jonasgerdes.stoppelmap.preparation

import com.jonasgerdes.stoppelmap.preperation.entity.*

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
)
