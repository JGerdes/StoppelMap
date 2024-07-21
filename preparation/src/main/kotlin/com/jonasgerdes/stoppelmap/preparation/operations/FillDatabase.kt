package com.jonasgerdes.stoppelmap.preparation.operations

/*import com.jonasgerdes.stoppelmap.data.Alias
import com.jonasgerdes.stoppelmap.data.Event
import com.jonasgerdes.stoppelmap.data.Item
import com.jonasgerdes.stoppelmap.data.Stall
import com.jonasgerdes.stoppelmap.data.StallItems
import com.jonasgerdes.stoppelmap.data.Stall_sub_types
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.Sub_types
import com.jonasgerdes.stoppelmap.data.model.database.PriceType
import com.jonasgerdes.stoppelmap.preparation.Data
import com.jonasgerdes.stoppelmap.preparation.Settings
import com.jonasgerdes.stoppelmap.preparation.transportation.generateRoutes
import com.jonasgerdes.stoppelmap.transportation.Departure
import com.jonasgerdes.stoppelmap.transportation.Departure_day
import com.jonasgerdes.stoppelmap.transportation.Price
import com.jonasgerdes.stoppelmap.transportation.Route
import com.jonasgerdes.stoppelmap.transportation.Station
import com.jonasgerdes.stoppelmap.transportation.model.Price.PriceLabel*/
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.shared.Fee
import com.jonasgerdes.stoppelmap.data.shared.Image
import com.jonasgerdes.stoppelmap.data.shared.Localized_string
import com.jonasgerdes.stoppelmap.data.shared.PreferredTheme
import com.jonasgerdes.stoppelmap.preparation.Settings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FillDatabase : KoinComponent {

    private val settings: Settings by inject()
    private val database: StoppelMapDatabase by inject()

    operator fun invoke() {

        database.transaction {
            database.feeQueries.insert(
                Fee(
                    referenceSlug = "generic_fee",
                    nameKey = "generic_fee_name",
                    price = 250L
                )
            )
            database.feeQueries.insert(
                Fee(
                    referenceSlug = "entry_fee",
                    nameKey = "entry_fee_name",
                    price = 999L
                )
            )
            database.localized_stringQueries.insert(
                Localized_string(
                    key = "generic_fee_name",
                    string = "Generic Fee",
                    locale = "en"
                )
            )
            database.localized_stringQueries.insert(
                Localized_string(
                    key = "generic_fee_name",
                    string = "Allgemeine Gebühr",
                    locale = "de"
                )
            )
            database.localized_stringQueries.insert(
                Localized_string(
                    key = "entry_fee_name",
                    string = "Entry",
                    locale = "en"
                )
            )
            database.localized_stringQueries.insert(
                Localized_string(
                    key = "entry_fee_name",
                    string = "Eintritt",
                    locale = "de"
                )
            )

            /*database.map_entityQueries.insert(Map_entity(
                slug = "wilde_maus",
                name = "Wilde Maus",
                type = MapEntityType.Ride,
                sub_type =
            ))*/

            database.localized_stringQueries.insert(
                Localized_string(
                    key = "image_caption",
                    string = "Image caption",
                    locale = "en"
                )
            )
            database.localized_stringQueries.insert(
                Localized_string(
                    key = "image_caption",
                    string = "Bildbeschreibung",
                    locale = "de"
                )
            )
            database.localized_stringQueries.insert(
                Localized_string(
                    key = "image_copyright",
                    string = "Image copyright",
                    locale = "en"
                )
            )
            database.localized_stringQueries.insert(
                Localized_string(
                    key = "image_copyright",
                    string = "Urheber",
                    locale = "de"
                )
            )

            database.imageQueries.insert(
                Image(
                    url = "https://example.com/image.jpg",
                    captionKey = "image_caption",
                    copyrightKey = "image_copyright",
                    blurHash = "fbkj349f",
                    preferredTheme = PreferredTheme.Light
                )
            )

            database.imageQueries.insert(
                Image(
                    url = "https://example.com/image2.jpg",
                    captionKey = null,
                    copyrightKey = null,
                    blurHash = "fbkj349f",
                    preferredTheme = PreferredTheme.Light
                )
            )
        }

        /* val data = Data().apply {
             parseGeoJson(
                 input = settings.geoJsonInput,
                 output = settings.geoJsonOutput,
                 descriptionFolder = null
             )

             parseEventSchedule(
                 settings.manualEventsFile,
                 settings.fetchedEventsFile,
             )
         }

         database.stallQueries.transaction {
             data.stalls.forEach {
                 database.stallQueries.insert(
                     Stall(
                         slug = it.slug,
                         type = it.type,
                         name = it.name,
                         center_lng = it.centerLng!!,
                         center_lat = it.centerLat!!,
                         isSearchable = it.isSearchable
                     )
                 )
             }
         }

         database.sub_typesQueries.transaction {
             data.subTypes.forEach {
                 database.sub_typesQueries.insert(
                     Sub_types(
                         slug = it.slug,
                         name = it.name
                     )
                 )
             }
         }

         database.stall_sub_typesQueries.transaction {
             data.stallSubTypes.forEach {
                 database.stall_sub_typesQueries.insert(
                     Stall_sub_types(
                         stall = it.stall,
                         sub_type = it.subType
                     )
                 )
             }
         }

         database.eventQueries.transaction {
             data.events.forEach {
                 database.eventQueries.insert(
                     Event(
                         slug = it.slug,
                         name = it.name,
                         location = it.locationName,
                         start = it.start,
                         end = it.end,
                         description = it.description,
                         isOfficial = it.isOfficial
                     )
                 )
             }
         }

         database.itemQueries.transaction {
             data.items.values.flatten().forEach {
                 database.itemQueries.insert(
                     Item(
                         slug = it.slug,
                         name = it.name
                     )
                 )
             }
         }

         database.stallItemsQueries.transaction {
             data.stallItems.forEach {
                 database.stallItemsQueries.insert(
                     StallItems(
                         stall = it.stall,
                         item = it.item
                     )
                 )
             }
         }

         database.aliasQueries.transaction {
             data.alias.forEach {
                 database.aliasQueries.insert(
                     Alias(
                         alias = it.alias,
                         stall = it.stall,
                     )
                 )
             }
         }

         val routes = generateRoutes()
         System.out.println("Created ${routes.size} transportation routes")

         database.routeQueries.transaction {
             routes.forEach {
                 database.routeQueries.insert(
                     Route(
                         slug = it.id,
                         title = it.title,
                         additionalInfo = it.additionalInfo,
                         type = it.type,
                     )
                 )
             }
         }

         database.stationQueries.transaction {
             routes.forEach { route ->
                 route.stations.forEach {
                     database.stationQueries.insert(
                         Station(
                             slug = it.id,
                             route = route.id,
                             title = it.title,
                             lng = null,
                             lat = null,
                             isDestination = it.isDestination,
                             isReturn = false,
                             annotateAsNew = it.annotateAsNew,
                         )
                     )
                 }
                 route.returnStations.forEach {
                     database.stationQueries.insert(
                         Station(
                             slug = it.id,
                             route = route.id,
                             title = it.title,
                             lng = null,
                             lat = null,
                             isDestination = it.isDestination,
                             isReturn = true,
                             annotateAsNew = it.annotateAsNew,
                         )
                     )
                 }
             }
         }

         database.departure_dayQueries.transaction {
             routes.forEach { route ->
                 route.allStations().forEach { station ->
                     station.departures.forEach {
                         database.departure_dayQueries.insert(
                             Departure_day(
                                 id = station.id + "-" + it.day.toString(),
                                 station = station.id,
                                 day = it.day,
                                 laterDeparturesOnDemand = it.laterDeparturesOnDemand
                             )
                         )
                     }
                 }
             }
         }
         database.departureQueries.transaction {
             routes.forEach { route ->
                 route.allStations().forEach { station ->
                     station.departures.forEach { day ->
                         day.departures.forEach {
                             database.departureQueries.insert(
                                 Departure(
                                     departureDay = station.id + "-" + day.day.toString(),
                                     time = it.time,
                                     annotation_ = it.annotation
                                 )
                             )
                         }
                     }
                 }
             }
         }

         database.priceQueries.transaction {
             routes.forEach { route ->
                 route.allStations().forEach { station ->
                     station.prices.forEach {
                         database.priceQueries.insert(
                             Price(
                                 station = station.id,
                                 type = when (it.label) {
                                     PriceLabel.Adult -> PriceType.Adult
                                     is PriceLabel.Children -> PriceType.Child
                                     is PriceLabel.Reduced -> PriceType.Reduced
                                 },
                                 amount = it.amountInCents.toLong(),
                                 minAge = (it.label as? PriceLabel.Children)?.minAge?.toLong(),
                                 maxAge = (it.label as? PriceLabel.Children)?.maxAge?.toLong(),
                                 additionalInfo = null,
                             )
                         )
                     }
                 }
             }
         }*/
    }
}

private fun com.jonasgerdes.stoppelmap.transportation.model.Route.allStations() =
    (stations + returnStations)
