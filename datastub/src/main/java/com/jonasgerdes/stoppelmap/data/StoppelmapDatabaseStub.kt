package com.jonasgerdes.stoppelmap.data

import com.jonasgerdes.stoppelmap.data.dao.*
import com.jonasgerdes.stoppelmap.model.events.Event
import com.jonasgerdes.stoppelmap.model.map.Alias
import com.jonasgerdes.stoppelmap.model.map.Item
import com.jonasgerdes.stoppelmap.model.map.Stall
import com.jonasgerdes.stoppelmap.model.map.SubType
import com.jonasgerdes.stoppelmap.model.transportation.Departure
import com.jonasgerdes.stoppelmap.model.transportation.Route
import com.jonasgerdes.stoppelmap.model.transportation.Station
import com.jonasgerdes.stoppelmap.model.transportation.TransportPrice

class StoppelmapDatabaseStub : StoppelmapDatabase {
    private val stalls = mutableListOf<Stall>()
    private val aliases = mutableListOf<Alias>()
    private val types = mutableListOf<SubType>()
    private val events = mutableListOf<Event>()
    private val items = mutableListOf<Item>()
    private val prices = mutableListOf<TransportPrice>()
    private val routes = mutableListOf<Route>()
    private val stations = mutableListOf<Station>()
    private val departures = mutableListOf<Departure>()
    private val stallTypes = mutableListOf<Pair<Stall, SubType>>()
    private val stallItems = mutableListOf<Pair<Stall, Item>>()

    fun empty() = apply {
        stalls.clear()
        types.clear()
        aliases.clear()
        events.clear()
        items.clear()
        prices.clear()
        routes.clear()
        stations.clear()
        departures.clear()
    }

    fun with(vararg stalls: Stall) = apply {
        this.stalls.addAll(stalls)
    }

    fun with(vararg aliases: Alias) = apply {
        this.aliases.addAll(aliases)
    }

    fun with(stall: Stall? = null, vararg types: SubType) = apply {
        this.types.addAll(types)
        if (stall != null) {
            this.stallTypes.addAll(types.map { stall to it })
            if (!stalls.contains(stall)) stalls.add(stall)
        }
    }

    fun with(vararg events: Event) = apply {
        this.events.addAll(events)
    }

    fun with(stall: Stall? = null, vararg items: Item) = apply {
        this.items.addAll(items)
        if (stall != null) {
            this.stallItems.addAll(items.map { stall to it })
            if (!stalls.contains(stall)) stalls.add(stall)
        }
    }

    fun with(vararg prices: TransportPrice) = apply {
        this.prices.addAll(prices)
    }

    fun with(vararg routes: Route) = apply {
        this.routes.addAll(routes)
    }

    fun with(vararg stations: Station) = apply {
        this.stations.addAll(stations)
    }

    fun with(vararg departures: Departure) = apply {
        this.departures.addAll(departures)
    }

    override fun stallsDao(): StallDao = StallDaoStub(stalls, aliases, stallItems, stallTypes)
    override fun stallTypeDao(): StallTypeDao = StallTypeDaoStub(types)
    override fun itemDao(): ItemDao = ItemDaoStub(items)
    override fun eventDao(): EventDao = EventDaoStub(events)
    override fun routeDao(): RouteDao = RouteDaoStub(routes)
    override fun stationDao(): StationDao = StationDaoStub(stations)
    override fun departureDao(): DepartureDao = DepartureDaoStub(departures)
    override fun priceDao(): PriceDao = PriceDaoStub(prices)
}