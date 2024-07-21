package com.jonasgerdes.stoppelmap.data

import app.cash.sqldelight.ColumnAdapter
import com.jonasgerdes.stoppelmap.data.map.MapEntityType
import com.jonasgerdes.stoppelmap.data.map.Map_entity
import com.jonasgerdes.stoppelmap.data.schedule.Event
import com.jonasgerdes.stoppelmap.data.schedule.Event_person
import com.jonasgerdes.stoppelmap.data.schedule.ParticipantType
import com.jonasgerdes.stoppelmap.data.shared.Image
import com.jonasgerdes.stoppelmap.data.shared.PreferredTheme
import com.jonasgerdes.stoppelmap.data.transportation.Departure
import com.jonasgerdes.stoppelmap.data.transportation.Departure_day
import com.jonasgerdes.stoppelmap.data.transportation.Route
import com.jonasgerdes.stoppelmap.data.transportation.TransportationType
import org.koin.dsl.module

val dataModule = module {
    single {
        StoppelMapDatabase(
            driver = get(),
            departureAdapter = Departure.Adapter(
                timeAdapter = localDateTimeAdapter,
            ),
            departure_dayAdapter = Departure_day.Adapter(
                dayAdapter = localDateAdapter,
            ),
            eventAdapter = Event.Adapter(
                startAdapter = localDateTimeAdapter,
                endAdapter = localDateTimeAdapter,
            ),
            event_personAdapter = Event_person.Adapter(
                typeAdapter = object : ColumnAdapter<ParticipantType, String> {
                    override fun decode(databaseValue: String) =
                        ParticipantType.entries.first { it.id == databaseValue }

                    override fun encode(value: ParticipantType) = value.id
                }
            ),
            imageAdapter = Image.Adapter(
                preferredThemeAdapter = object : ColumnAdapter<PreferredTheme, String> {
                    override fun decode(databaseValue: String) =
                        PreferredTheme.entries.first { it.id == databaseValue }

                    override fun encode(value: PreferredTheme) = value.id
                }
            ),
            map_entityAdapter = Map_entity.Adapter(
                typeAdapter = object : ColumnAdapter<MapEntityType, String> {
                    override fun decode(databaseValue: String) =
                        MapEntityType.entries.first { it.id == databaseValue }

                    override fun encode(value: MapEntityType) = value.id
                }
            ),
            routeAdapter = Route.Adapter(
                typeAdapter = object : ColumnAdapter<TransportationType, String> {
                    override fun decode(databaseValue: String) =
                        TransportationType.entries.first { it.id == databaseValue }

                    override fun encode(value: TransportationType) = value.id
                }
            ),
        )
    }
}
