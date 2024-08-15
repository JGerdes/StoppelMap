package com.jonasgerdes.stoppelmap.map.data

import com.jonasgerdes.stoppelmap.data.map.MapEntityType
import com.jonasgerdes.stoppelmap.data.map.Map_entityQueries
import com.jonasgerdes.stoppelmap.data.map.Map_entity_tagQueries
import com.jonasgerdes.stoppelmap.data.map.Sub_typeQueries
import com.jonasgerdes.stoppelmap.data.shared.AliasQueries
import com.jonasgerdes.stoppelmap.data.shared.OfferQueries
import com.jonasgerdes.stoppelmap.map.model.BoundingBox
import com.jonasgerdes.stoppelmap.map.model.FullMapEntity
import com.jonasgerdes.stoppelmap.map.model.GeoData
import com.jonasgerdes.stoppelmap.map.model.Location
import com.jonasgerdes.stoppelmap.map.model.MapIcon
import com.jonasgerdes.stoppelmap.map.model.Offer
import com.jonasgerdes.stoppelmap.map.model.Offer.Companion.barProducts
import com.jonasgerdes.stoppelmap.map.model.Offer.Companion.imbissProducts
import com.jonasgerdes.stoppelmap.map.model.StallSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class MapEntityRepository(
    private val mapEntityQueries: Map_entityQueries,
    private val aliasQueries: AliasQueries,
    private val subTypeQueries: Sub_typeQueries,
    private val offerQueries: OfferQueries,
    private val mapMapTagQueries: Map_entity_tagQueries,
) {

    suspend fun searchMapEntitiesByName(query: String): List<String> = withContext(Dispatchers.IO) {
        mapEntityQueries.searchByName(query).executeAsList()
    }

    suspend fun getSummaryBySlugs(slugs: Set<String>) = withContext(Dispatchers.IO) {
        val entities = mapEntityQueries.getSummaryBySlug(slugs).executeAsList()
        val types = aliasQueries.getByReferenceSlug(entities.map { it.type.id }.toSet()).executeAsList()
            .groupBy({ it.referenceSlug }, { it.string })
        val subTypes = subTypeQueries.getSubTypeBySlugs(entities.mapNotNull { it.sub_type }.toSet()).executeAsList()
        entities.map { summary ->
            StallSummary(
                slug = summary.slug,
                name = summary.name ?: getNameFallback(
                    summary.slug,
                    null,
                    types[summary.type.id]?.first()
                ),
                icon = summary.type.getIcon(),
                typeName = types[summary.type.id]?.first(),
                subTypeName = subTypes.firstOrNull { it.slug == summary.sub_type }?.name
            )
        }
    }

    private fun getNameFallback(
        stallSlug: String,
        subTypeName: String?,
        typeName: String?,
    ): String {
        val offers = offerQueries.offersByReferenceSlug(stallSlug, ::Offer).executeAsList()
            .filter { !barProducts.contains(it.productSlug) }
        return when {
            imbissProducts.all { productSlug -> offers.any { it.productSlug == productSlug } } -> "Imbiss" //TODO: localize
            offers.isNotEmpty() -> offers.first().name + if (offers.size > 1) " etc." else ""
            subTypeName != null -> subTypeName
            typeName != null -> typeName
            else -> "Stand" //TODO: localize
        }
    }

    suspend fun getDetailedMapEntity(slug: String): FullMapEntity? = withContext(Dispatchers.IO) {
        val mapEntity = mapEntityQueries.getFullBySlug(slug).executeAsList().firstOrNull() ?: return@withContext null
        val type =
            aliasQueries.getByReferenceSlug(setOf(mapEntity.type.id)).executeAsList().maxByOrNull { it.string.length }
        val subType = mapEntity.sub_type?.let { subTypeQueries.getSubTypeBySlugs(setOf(it)) }?.executeAsOneOrNull()
        val offers = offerQueries.offersByReferenceSlug(slug, ::Offer).executeAsList()
        val displayName = mapEntity.name ?: getNameFallback(slug, subType?.name, type?.string)
        FullMapEntity(
            slug = slug,
            name = displayName,
            typeName = type?.string?.takeIf { it != displayName },
            type = mapEntity.type,
            subType = subType?.name?.takeIf { it != displayName },
            location = Location(lat = mapEntity.latitude, lng = mapEntity.longitude),
            description = mapEntity.description,
            bounds = BoundingBox(
                southLat = mapEntity.southLatitude,
                westLng = mapEntity.westLongitude,
                northLat = mapEntity.northLatitude,
                eastLng = mapEntity.eastLongitude,
            ),
            icon = mapEntity.type.getIcon(),
            offers = offers
        )
    }

    suspend fun searchByAlias(query: String) = withContext(Dispatchers.IO) {
        mapEntityQueries.searchByAlias(query).executeAsList()
    }

    suspend fun getSummaryBySubType(subtypes: Set<String>): Map<String, List<StallSummary>> =
        withContext(Dispatchers.IO) {
            mapEntityQueries.getBySubTypes(subtypes).executeAsList().filter { it.sub_type != null }
        }.let { bySubType ->
            val summaries = getSummaryBySlugs(bySubType.map { it.slug }.toSet())
            bySubType.groupBy({ it.sub_type!! }, { summaries.first { summary -> it.slug == summary.slug } })
        }

    suspend fun searchAliasBy(referenceSlugs: Set<String>, query: String) = withContext(Dispatchers.IO) {
        aliasQueries.searchByReference(referenceSlugs, query).executeAsList()
    }

    suspend fun searchByType(type: MapEntityType) = withContext(Dispatchers.IO) {
        mapEntityQueries.searchType(type).executeAsList()
    }

    suspend fun getGeoDataBySlugs(slugs: Set<String>) = withContext(Dispatchers.IO) {
        mapEntityQueries.getGeoDataBySlugs(slugs).executeAsList()
            .map {
                it.slug to GeoData(
                    center = Location(lat = it.latitude, lng = it.longitude),
                    boundingBox = BoundingBox(
                        southLat = it.southLatitude,
                        westLng = it.westLongitude,
                        northLat = it.northLatitude,
                        eastLng = it.eastLongitude
                    )
                )
            }.toMap()
    }

    suspend fun getByTag(tagSlugs: Set<String>) = withContext(Dispatchers.IO) {
        mapMapTagQueries.getByTag(tagSlugs).executeAsList()
    }
}

private fun MapEntityType.getIcon() = when (this) {
    MapEntityType.Misc -> MapIcon.Misc
    MapEntityType.Bar -> MapIcon.Bar
    MapEntityType.Building -> MapIcon.Restaurant
    MapEntityType.CandyStall -> MapIcon.CandyStall
    MapEntityType.Entrance -> MapIcon.Entrance
    MapEntityType.Exhibition -> MapIcon.Expo
    MapEntityType.FoodStall -> MapIcon.FoodStall
    MapEntityType.GameStall -> MapIcon.GameStall
    MapEntityType.Info -> MapIcon.Misc
    MapEntityType.Parking -> MapIcon.Parking
    MapEntityType.Platform -> MapIcon.Platform
    MapEntityType.Restaurant -> MapIcon.Restaurant
    MapEntityType.Restroom -> MapIcon.Restroom
    MapEntityType.Ride -> MapIcon.Ride
    MapEntityType.SellerStall -> MapIcon.SellerStall
    MapEntityType.Station -> MapIcon.Station
    MapEntityType.Taxi -> MapIcon.Taxi
}