package com.jonasgerdes.stoppelmap.map.data

import com.jonasgerdes.stoppelmap.data.map.MapEntityType
import com.jonasgerdes.stoppelmap.data.map.Map_entityQueries
import com.jonasgerdes.stoppelmap.data.map.Sub_typeQueries
import com.jonasgerdes.stoppelmap.data.shared.AliasQueries
import com.jonasgerdes.stoppelmap.map.model.BoundingBox
import com.jonasgerdes.stoppelmap.map.model.FullMapEntity
import com.jonasgerdes.stoppelmap.map.model.Location
import com.jonasgerdes.stoppelmap.map.model.MapIcon
import com.jonasgerdes.stoppelmap.map.model.StallSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class MapEntityRepository(
    val mapEntityQueries: Map_entityQueries,
    val aliasQueries: AliasQueries,
    val subTypeQueries: Sub_typeQueries,
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
                name = summary.name ?: types[summary.type.id]?.first() ?: summary.type.id,
                icon = summary.type.getIcon(),
                typeName = types[summary.type.id]?.first(),
                subTypeName = subTypes.firstOrNull { it.slug == summary.sub_type }?.name
            )
        }
    }

    suspend fun getDetailedMapEntity(slug: String): FullMapEntity = withContext(Dispatchers.IO) {
        val mapEntity = mapEntityQueries.getFullBySlug(slug).executeAsOne()
        val type = aliasQueries.getByReferenceSlug(setOf(mapEntity.type.id)).executeAsOneOrNull()
        val subType = mapEntity.sub_type?.let { subTypeQueries.getSubTypeBySlugs(setOf(it)) }?.executeAsOneOrNull()
        val displayName = mapEntity.name ?: subType?.name ?: type?.string ?: mapEntity.type.id
        FullMapEntity(
            slug = slug,
            name = displayName,
            type = type?.string?.takeIf { it != displayName },
            subType = subType?.name?.takeIf { it != displayName },
            location = Location(lat = mapEntity.latitude, lng = mapEntity.longitude),
            description = mapEntity.description,
            bounds = BoundingBox(
                southLat = mapEntity.southLatitude,
                westLng = mapEntity.westLongitude,
                northLat = mapEntity.northLatitude,
                eastLng = mapEntity.eastLongitude,
            )
        )
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