package com.jonasgerdes.stoppelmap.data

import app.cash.sqldelight.EnumColumnAdapter
import com.jonasgerdes.stoppelmap.data.model.database.PriceType
import com.jonasgerdes.stoppelmap.data.model.database.RouteType
import com.jonasgerdes.stoppelmap.transportation.Departure
import com.jonasgerdes.stoppelmap.transportation.Departure_day
import com.jonasgerdes.stoppelmap.transportation.Price
import com.jonasgerdes.stoppelmap.transportation.Route
import org.koin.dsl.module

val dataModule = module {
    single {
        StoppelMapDatabase(
            driver = get(),
            eventAdapter = Event.Adapter(
                startAdapter = localDateTimeAdapter,
                endAdapter = localDateTimeAdapter
            ),
            routeAdapter = Route.Adapter(
                typeAdapter = EnumColumnAdapter<RouteType>()
            ),
            departure_dayAdapter = Departure_day.Adapter(
                dayAdapter = localDateAdapter
            ),
            departureAdapter = Departure.Adapter(
                timeAdapter = localDateTimeAdapter
            ),
            priceAdapter = Price.Adapter(
                typeAdapter = EnumColumnAdapter<PriceType>()
            )
        )
    }
}
