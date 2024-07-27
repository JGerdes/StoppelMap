package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.map.MapEntityType
import com.jonasgerdes.stoppelmap.data.map.Map_entity
import com.jonasgerdes.stoppelmap.data.shared.Bounding_box
import com.jonasgerdes.stoppelmap.data.shared.Fee
import com.jonasgerdes.stoppelmap.data.shared.Location
import com.jonasgerdes.stoppelmap.data.shared.Offer
import com.jonasgerdes.stoppelmap.dto.data.Map
import com.jonasgerdes.stoppelmap.dto.data.MapEntityType as DtoMapEntityType

internal fun StoppelMapDatabase.addMapData(data: Map) {
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
                priority = mapEntity.priority.toLong(),
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
        bounding_boxQueries.insert(
            Bounding_box(
                referenceSlug = mapEntity.slug,
                southLatitude = mapEntity.bbox.southLat,
                westLongitude = mapEntity.bbox.westLng,
                northLatitude = mapEntity.bbox.northLat,
                eastLongitude = mapEntity.bbox.eastLng,
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
                        index.toString().padStart(2, '0'),
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