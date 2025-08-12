package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.dto.Locales.de
import com.jonasgerdes.stoppelmap.dto.data.Alias
import com.jonasgerdes.stoppelmap.dto.data.BoundingBox
import com.jonasgerdes.stoppelmap.dto.data.Fee
import com.jonasgerdes.stoppelmap.dto.data.Image
import com.jonasgerdes.stoppelmap.dto.data.Location
import com.jonasgerdes.stoppelmap.dto.data.MapEntity
import com.jonasgerdes.stoppelmap.dto.data.MapEntityType
import com.jonasgerdes.stoppelmap.dto.data.Offer
import com.jonasgerdes.stoppelmap.dto.data.Operator
import com.jonasgerdes.stoppelmap.dto.data.PreferredTheme
import com.jonasgerdes.stoppelmap.dto.data.Website
import com.jonasgerdes.stoppelmap.preparation.definitions.SubTypeSlugs
import com.jonasgerdes.stoppelmap.preparation.definitions.TagSlugs
import com.jonasgerdes.stoppelmap.preparation.definitions.foodProducts
import com.jonasgerdes.stoppelmap.preparation.definitions.gameSubTypes
import com.jonasgerdes.stoppelmap.preparation.definitions.rideSubTypes
import com.jonasgerdes.stoppelmap.preparation.definitions.typeAliases
import com.jonasgerdes.stoppelmap.preparation.util.center
import com.jonasgerdes.stoppelmap.preparation.util.max
import com.jonasgerdes.stoppelmap.preparation.util.min
import com.jonasgerdes.stoppelmap.preparation.util.position
import com.jonasgerdes.stoppelmap.preperation.asSlug
import com.jonasgerdes.stoppelmap.preperation.splitBy
import com.jonasgerdes.stoppelmap.preperation.splitSafe
import com.jonasgerdes.stoppelmap.preperation.toShortHash
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import mobi.waterdog.kgeojson.Feature
import mobi.waterdog.kgeojson.FeatureCollection
import mobi.waterdog.kgeojson.GeoJson
import mobi.waterdog.kgeojson.Point
import mobi.waterdog.kgeojson.Polygon
import java.io.File
import kotlin.system.exitProcess

@OptIn(ExperimentalSerializationApi::class)
class ParseGeoData(
    private val input: File,
    private val output: File,
    private val descriptionFolder: File?,
    private val debugLogging: Boolean = false,
) {
    val mapEntities = mutableListOf<MapEntity>()
    val operators = mutableListOf<Operator>()
    private val json = Json { ignoreUnknownKeys = true }

    private val typesToProcess = setOf("building", "public_transport", "amenity")

    operator fun invoke() {
        val geoJson: FeatureCollection =
            json.decodeFromStream<GeoJson>(input.inputStream()) as FeatureCollection

        geoJson
            .features
            .forEach {
                val feature = it as Feature
                if (feature.properties.keys.any(typesToProcess::contains)
                    && feature.properties["building"] != "yes"
                ) {
                    val updated = feature.parseMapEntity()
                    if (mapEntities.any { it.slug == updated.slug }) {
                        System.err.println("ERR: Duplicate slug: ${updated.slug}")
                        exitProcess(0)
                    }
                    mapEntities.add(updated)
                    feature.properties["slug"] = updated.slug
                    feature.properties["priority"] = updated.priority.toString()
                }
            }
        Json.encodeToStream(geoJson as GeoJson, output.outputStream())
    }

    private fun Feature.parseMapEntity(): MapEntity {
        if (debugLogging) println("Parse feature (${properties}")
        val center = when (geometry) {
            is Point -> geometry.position
            is Polygon -> geometry.coordinates.flatten().center()
            else -> throw InvalidGeoJsonFeature(this, "center")
        }

        val min = when (geometry) {
            is Point -> geometry.position
            is Polygon -> geometry.coordinates.flatten().min()
            else -> throw InvalidGeoJsonFeature(this, "min")
        }

        val max = when (geometry) {
            is Point -> geometry.position
            is Polygon -> geometry.coordinates.flatten().max()
            else -> throw InvalidGeoJsonFeature(this, "max")
        }
        val type = MapEntityType.fromId(
            properties.firstValue(typesToProcess)
                ?.replace("-", "_")
                ?.replace("house", "building")
                ?: throw InvalidGeoJsonFeature(this, "type")
        ) ?: throw InvalidGeoJsonFeature(this, "type")

        val mapEntity = MapEntity(
            slug = "placeholder",
            name = properties["name"],
            type = type,
            subType = when (type) {
                MapEntityType.Bar -> {
                    SubTypeSlugs.partyTent.takeIf { properties["isTent"] == "yes" }
                }

                MapEntityType.GameStall -> {
                    gameSubTypes.firstOrNull {
                        properties.getOrDefault("game_${it.slug}", null) == "yes"
                    }?.slug.also { if (it == null) println("WARN: No matching game type found for $properties") }
                }

                MapEntityType.Ride -> {
                    rideSubTypes.firstOrNull {
                        properties["type"] == it.slug
                    }?.slug.also { if (it == null) println("WARN: No matching ride type found for $properties") }
                }

                MapEntityType.Restroom -> {
                    when {
                        properties["accessible"] == "yes" -> SubTypeSlugs.accessibleRestroom
                        properties["women"] == "yes" -> SubTypeSlugs.womensRestroom
                        properties["men"] == "yes" -> SubTypeSlugs.urinalRestroom
                        else -> null
                    }
                }

                else -> null
            },
            operator = properties["operator"]?.let { operator ->
                if (operator.contains(";")) {
                    val (slug, name, websites) = operator.splitSafe(";", 3)
                    if (operators.none { it.slug == slug }) {
                        operators += Operator(
                            slug = slug!!, name = name!!, websites = websites?.split("|")?.map {
                                Website(url = it)
                            } ?: emptyList()
                        )
                    }
                    slug
                } else {
                    val slug = operator.asSlug()
                    if (operators.none { it.slug == slug }) {
                        operators += Operator(slug = slug, name = operator, websites = emptyList())
                    }
                    slug
                }
            },
            aliases = properties["alias"]?.splitBy(";", "|") {
                Alias(string = it[0], locale = it.getOrNull(1))
            } ?: emptyList(),
            description = properties["description"]?.let { mapOf(de to it) },
            center = Location(
                lat = center.lat,
                lng = center.lng,
            ),
            bbox = BoundingBox(
                southLat = min.lat,
                westLng = min.lng,
                northLat = max.lat,
                eastLng = max.lng
            ),
            priority = properties["priority"]?.toIntOrNull() ?: 95,
            tags = listOfNotNull(
                TagSlugs.forKids.takeIf { properties["forKids"] == "yes" },
                TagSlugs.wheelchairAccessible.takeIf { properties["accessible"] == "yes" },
                TagSlugs.vegan.takeIf { properties["vegan_options"] == "yes" },
            ),
            offers = foodProducts
                .filter { properties.getOrDefault(it.slug, null) == "yes" }
                .map { product ->
                    Offer(
                        productSlug = product.slug,
                        modifier = null,
                        price = null,
                        visible = false
                    )
                },
            services = properties["services"]?.split(",")?.map {
                it
            } ?: emptyList(),
            admissionFees = properties["fees"]?.split(";")
                ?.mapNotNull { fee ->
                    val (price, names) = fee.splitSafe("|", 2)
                    if (price == null || names == null) null
                    else {
                        Fee(
                            name = names.splitBy("+", ">") {
                                it[0] to it[1]
                            }.toMap(),
                            price = price.toInt()
                        )
                    }
                } ?: emptyList(),
            images = properties["pictures"]?.split(";")
                ?.map { image ->
                    val (url, blurHash, captions, copyrights, preferredTheme)
                            = image.splitSafe("|", 3)
                    Image(
                        url = url!!,
                        caption = captions?.splitBy("+", ">") {
                            it[0] to it[1]
                        }?.toMap(),
                        copyright = copyrights?.splitBy("+", ">") {
                            it[0] to it[1]
                        }?.toMap(),
                        blurHash = blurHash!!,
                        preferredTheme = preferredTheme?.let(PreferredTheme::fromId)
                    )
                } ?: emptyList(),
            websites = properties["website"]?.split("|")?.map {
                Website(url = it)
            } ?: emptyList(),
            isSearchable = properties["searchable"]?.toBooleanStrictOrNull() ?: true

        )

        return mapEntity.copy(
            slug = properties["slug"]?.replace("_", "-") ?: mapEntity.createSlug()
        )
    }
}


fun Map<String, String>.firstValue(
    keys: Set<String>
) = keys
    .firstOrNull { !get(it).isNullOrBlank() }
    ?.let { get(it) }

class InvalidGeoJsonFeature(feature: Feature, propertyName: String? = null) : Exception(
    "Invalid${propertyName?.let { " property $it on" }} feature with name ${feature.properties["name"]}, slug ${feature.properties["slug"]}. Feature: $feature"
)

private fun MapEntity.createSlug(): String {
    val slug = name?.asSlug()?.let {
        if (type in listOf(
                MapEntityType.Bar,
                MapEntityType.Misc,
                MapEntityType.Info,
                MapEntityType.Platform
            )
        ) it else "${type.toGermanSlug()}-$it"
    } ?: "${type.toGermanSlug()}-${"${center.lat}, ${center.lng}".toShortHash()}"

    return slug.lowercase()
}

private fun MapEntityType.toGermanSlug() = typeAliases
    .firstOrNull { it.type == this }
    ?.aliases
    ?.firstOrNull { it.locale == de }
    ?.string
    ?.asSlug()
    ?: id