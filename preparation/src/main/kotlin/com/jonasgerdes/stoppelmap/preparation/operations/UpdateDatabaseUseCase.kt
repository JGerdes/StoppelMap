package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.map.MapEntityType
import com.jonasgerdes.stoppelmap.data.map.Map_entity
import com.jonasgerdes.stoppelmap.data.map.Sub_type
import com.jonasgerdes.stoppelmap.data.schedule.Event
import com.jonasgerdes.stoppelmap.data.schedule.ParticipantType
import com.jonasgerdes.stoppelmap.data.schedule.Person
import com.jonasgerdes.stoppelmap.data.shared.Alias
import com.jonasgerdes.stoppelmap.data.shared.Fee
import com.jonasgerdes.stoppelmap.data.shared.Image
import com.jonasgerdes.stoppelmap.data.shared.Localized_string
import com.jonasgerdes.stoppelmap.data.shared.Location
import com.jonasgerdes.stoppelmap.data.shared.Offer
import com.jonasgerdes.stoppelmap.data.shared.PreferredTheme
import com.jonasgerdes.stoppelmap.data.shared.Product
import com.jonasgerdes.stoppelmap.data.shared.Service
import com.jonasgerdes.stoppelmap.data.shared.Website
import com.jonasgerdes.stoppelmap.dto.Localized
import com.jonasgerdes.stoppelmap.dto.data.Map
import com.jonasgerdes.stoppelmap.dto.data.Schedule
import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData
import com.jonasgerdes.stoppelmap.dto.data.Image as DtoImage
import com.jonasgerdes.stoppelmap.dto.data.MapEntityType as DtoMapEntityType
import com.jonasgerdes.stoppelmap.dto.data.ParticipantType as DtoParticipantType
import com.jonasgerdes.stoppelmap.dto.data.PreferredTheme as DtoPreferredTheme

class UpdateDatabaseUseCase(
    private val data: StoppelMapData,
    private val stoppelMapDatabase: StoppelMapDatabase,
) {
    operator fun invoke() = with(stoppelMapDatabase) {
        transaction {
            clearData()

            metadataQueries.insert(
                version = data.version.toLong(),
                schema_version = data.schemaVersion.toLong(),
                note = data.note,
                season_year = data.seasonYear.toLong(),
                map_is_wip = data.map.isWorkInProgress,
                schedule_is_wip = data.schedule.isWorkInProgress,
                transport_is_wip = data.transportation.isWorkInProgress,
            )

            addDefinitions(data)
            addMapData(data.map)
            addScheduleData(data.schedule)
        }
    }

    private fun StoppelMapDatabase.addScheduleData(schedule: Schedule) {
        schedule.events.forEach { event ->
            eventQueries.insert(
                Event(
                    slug = event.slug,
                    name = event.name,
                    start = event.start,
                    end = event.end,
                    descriptionKey = event.description?.let {
                        addLocalizedString(it, event.slug, "description")
                    },
                    locationSlug = event.location,
                    isOfficial = event.isOfficial
                )
            )
            event.participants.forEach {
                event_personQueries.insert(
                    eventSlug = event.slug,
                    personSlug = it.person,
                    type = when (it.type) {
                        DtoParticipantType.Unknown -> ParticipantType.Unknown
                        DtoParticipantType.Band -> ParticipantType.Band
                        DtoParticipantType.DJ -> ParticipantType.DJ
                        DtoParticipantType.GuestOfHonor -> ParticipantType.GuestOfHonor
                        DtoParticipantType.Speaker -> ParticipantType.Speaker
                        null -> null
                    }
                )
            }
            addWebsites(event.slug, event.websites)
        }
    }

    private fun StoppelMapDatabase.addDefinitions(data: StoppelMapData) {
        data.definitions.tags.forEach {
            tagQueries.insert(
                slug = it.slug,
                nameKey = addLocalizedString(it.name, it.slug, "name")
            )
            addAliases(it.slug, it.aliases)
        }

        data.definitions.subTypes.forEach {
            sub_typeQueries.insert(
                Sub_type(
                    slug = it.slug,
                    nameKey = addLocalizedString(it.name, it.slug, "name")
                )
            )
            addAliases(it.slug, it.aliases)
        }

        data.definitions.products.forEach {
            productQueries.insert(
                Product(
                    slug = it.slug,
                    nameKey = addLocalizedString(it.name, it.slug, "name")
                )
            )
            addAliases(it.slug, it.aliases)
        }

        data.definitions.services.forEach {
            serviceQueries.insert(
                Service(
                    slug = it.slug,
                    nameKey = addLocalizedString(it.name, it.slug, "name"),
                    noteKey = addLocalizedString(it.name, it.slug, "note")
                )
            )
            addAliases(it.slug, it.aliases)
        }

        data.definitions.persons.forEach {
            personQueries.insert(
                Person(
                    slug = it.slug,
                    name = it.name,
                    descriptionKey = it.description?.let { description ->
                        addLocalizedString(description, it.slug, "description")
                    }
                )
            )
            addImages(it.slug, it.images)
            it.images.forEach { image ->
                person_imageQueries.insert(personSlug = it.slug, image = image.url)
            }
        }

        data.definitions.operators.forEach {
            operatorQueries.insert(
                slug = it.slug,
                name = it.name,
            )
            addWebsites(it.slug, it.websites)
        }
    }

    private fun StoppelMapDatabase.addMapData(data: Map) {
        data.entities.forEach { mapEntity ->
            map_entityQueries.insert(
                Map_entity(
                    slug = mapEntity.slug,
                    name = mapEntity.name,
                    type = mapEntity.type.toMapEntityType(),
                    sub_type = mapEntity.subType,
                    descriptionKey = mapEntity.description?.let {
                        addLocalizedString(
                            it,
                            mapEntity.slug,
                            "description"
                        )
                    },
                    operator_ = mapEntity.operator,
                    isSearchable = mapEntity.isSearchable,
                )
            )
            addAliases(mapEntity.slug, mapEntity.aliases)
            locationQueries.insert(
                Location(
                    referenceSlug = mapEntity.slug,
                    latitude = mapEntity.center.lat,
                    longitude = mapEntity.center.lng,
                )
            )
            mapEntity.tags.forEach {
                map_entity_tagQueries.insert(mapEntity.slug, it)
            }
            mapEntity.offers.forEach { offer ->
                offerQueries.insert(
                    Offer(
                        referenceSlug = mapEntity.slug,
                        productSlug = offer.productSlug,
                        modifierKey = offer.modifier?.let {
                            addLocalizedString(
                                it,
                                mapEntity.slug,
                                "offer",
                                offer.productSlug,
                                "modifier"
                            )
                        },
                        price = offer.price?.toLong(),
                        visible = offer.visible,
                    )
                )
            }
            mapEntity.services.forEach {
                map_entity_serviceQueries.insert(mapEntity.slug, it)
            }
            mapEntity.admissionFees.forEachIndexed { index, fee ->
                feeQueries.insert(
                    Fee(
                        referenceSlug = mapEntity.slug,
                        nameKey = addLocalizedString(
                            fee.name,
                            mapEntity.slug,
                            "admission",
                            index.toString().padStart(2),
                            "fee"
                        ),
                        price = fee.price.toLong()
                    )
                )
            }
            addImages(mapEntity.slug, mapEntity.images)
            mapEntity.images.forEach {
                map_entity_imageQueries.insert(mapEntity.slug, it.url)
            }
            addWebsites(mapEntity.slug, mapEntity.websites)
        }
    }
}

private fun StoppelMapDatabase.addWebsites(referenceSlug: String, websites: List<String>) {
    websites.forEach {
        websiteQueries.insert(
            Website(
                referenceSlug = referenceSlug,
                url = it
            )
        )
    }
}

private fun StoppelMapDatabase.addImages(referenceSlug: String, images: List<DtoImage>) {
    images.forEachIndexed { index, image ->
        imageQueries.insert(
            Image(
                url = image.url,
                captionKey = image.caption?.let {
                    addLocalizedString(
                        it,
                        referenceSlug,
                        "image",
                        index.toString().padStart(2),
                        "caption"
                    )
                },
                copyrightKey = image.copyright?.let {
                    addLocalizedString(
                        it,
                        referenceSlug,
                        "image",
                        index.toString().padStart(2),
                        "copyright"
                    )
                },
                blurHash = image.blurHash,
                preferredTheme = when (image.preferredTheme) {
                    DtoPreferredTheme.Light -> PreferredTheme.Light
                    DtoPreferredTheme.Dark -> PreferredTheme.Dark
                    null -> null
                }
            )
        )
    }
}

private fun StoppelMapDatabase.addAliases(
    referenceSlug: String,
    aliases: List<Localized<String>>?
) {
    aliases?.forEachIndexed { index, alias ->
        aliasQueries.insert(
            Alias(
                referenceSlug = referenceSlug,
                aliasKey = addLocalizedString(
                    alias,
                    referenceSlug,
                    "alias",
                    index.toString().padStart(2, '0')
                )
            )
        )
    }
}

private fun StoppelMapDatabase.addLocalizedString(
    localizedString: Localized<String>,
    vararg keyParts: String,
): String {
    val key = keyParts.joinToString(separator = "_")
    localizedString.entries.forEach {
        localized_stringQueries.insert(
            Localized_string(
                key = key,
                locale = it.key,
                string = it.value,
            )
        )
    }
    return key
}

private fun DtoMapEntityType.toMapEntityType() = when (this) {
    DtoMapEntityType.Misc -> MapEntityType.Misc
    DtoMapEntityType.Bar -> MapEntityType.Bar
    DtoMapEntityType.Building -> MapEntityType.Building
    DtoMapEntityType.CandyStall -> MapEntityType.CandyStall
    DtoMapEntityType.Entrance -> MapEntityType.Entrance
    DtoMapEntityType.Expo -> MapEntityType.Expo
    DtoMapEntityType.FoodStall -> MapEntityType.FoodStall
    DtoMapEntityType.GameStall -> MapEntityType.GameStall
    DtoMapEntityType.Info -> MapEntityType.Info
    DtoMapEntityType.Parking -> MapEntityType.Parking
    DtoMapEntityType.Platform -> MapEntityType.Platform
    DtoMapEntityType.Restaurant -> MapEntityType.Restaurant
    DtoMapEntityType.Restroom -> MapEntityType.Restroom
    DtoMapEntityType.Ride -> MapEntityType.Ride
    DtoMapEntityType.SellerStall -> MapEntityType.SellerStall
    DtoMapEntityType.Station -> MapEntityType.Station
    DtoMapEntityType.Taxi -> MapEntityType.Taxi
}


private fun StoppelMapDatabase.clearData() {
    aliasQueries.clear()
    departureQueries.clear()
    departure_dayQueries.clear()
    eventQueries.clear()
    event_personQueries.clear()
    feeQueries.clear()
    imageQueries.clear()
    localized_stringQueries.clear()
    locationQueries.clear()
    map_entityQueries.clear()
    map_entity_imageQueries.clear()
    map_entity_serviceQueries.clear()
    map_entity_tagQueries.clear()
    metadataQueries.clear()
    offerQueries.clear()
    operatorQueries.clear()
    personQueries.clear()
    person_imageQueries.clear()
    phone_numberQueries.clear()
    productQueries.clear()
    routeQueries.clear()
    serviceQueries.clear()
    stationQueries.clear()
    sub_typeQueries.clear()
    tagQueries.clear()
    websiteQueries.clear()
}