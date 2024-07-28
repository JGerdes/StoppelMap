package mobi.waterdog.kgeojson

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class GeoJson

@Serializable
sealed class AbstractFeature : GeoJson()

@Serializable
sealed class AbstractGeometry : GeoJson()

@Serializable
@SerialName("FeatureCollection")
class FeatureCollection(
    val features: MutableList<AbstractFeature>
) : GeoJson() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FeatureCollection

        if (features != other.features) return false

        return true
    }

    override fun hashCode(): Int {
        return features.hashCode()
    }
}

@Serializable
@SerialName("Feature")
class Feature(
    val geometry: AbstractGeometry,
    val properties: MutableMap<String, String> = mutableMapOf()
) : AbstractFeature() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Feature

        if (geometry != other.geometry) return false
        if (properties != other.properties) return false

        return true
    }

    override fun hashCode(): Int {
        var result = geometry.hashCode()
        result = 31 * result + properties.hashCode()
        return result
    }
}

@Serializable
@SerialName("Geometry")
sealed class Geometry : AbstractGeometry() {
    abstract val coordinates: List<*>
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Geometry

        if (coordinates != other.coordinates) return false

        return true
    }

    override fun hashCode(): Int {
        return coordinates.hashCode()
    }
}

@Serializable
@SerialName("Point")
class Point(override val coordinates: List<Double>) : Geometry() {
    init {
        require(coordinates.size == 2) { "Point must have exactly 2 Double coordinates. Example: [lng, lat]." }
    }
}

@Serializable
@SerialName("MultiPoint")
class MultiPoint(override val coordinates: List<Position>) : Geometry() {
    init {
        require(coordinates.size == 2) { "MultiPoint must have at least 2 coordinates." }
    }
}

@Serializable
@SerialName("LineString")
class LineString(override val coordinates: MutableList<Position>) : Geometry() {
    init {
        require(coordinates.size >= 2) { "LineString must have at least 2 coordinated." }
    }
}

@Serializable
@SerialName("MultiLineString")
// TODO receive a MutableList<LineString> and implement a custom KSerializer
class MultiLineString(override val coordinates: MutableList<MutableList<Position>>) : Geometry() {
    init {
        require(coordinates.all { it.size >= 2 }) { "Each LineString must have at least 2 coordinated." }
    }
}

/**
 * You can use this constructor directly but we recommend to use PolygonBuilder instead.
 *
 * From RFC7946 - https://tools.ietf.org/html/rfc7946
 *
 * A linear ring is a closed LineString with four or more positions.
 *
 * For type "Polygon", the "coordinates" member MUST be an array of
 * linear ring coordinate arrays.
 *
 * For Polygons with more than one of these rings, the first MUST be
 * the exterior ring, and any others MUST be interior rings.  The
 * exterior ring bounds the surface, and the interior rings (if
 * present) bound holes within the surface.
 *
 */
@Serializable
@SerialName("Polygon")
class Polygon(override val coordinates: MutableList<MutableList<Position>>) : Geometry() {
    init {
        require(coordinates.isNotEmpty()) { "Polygon must contain at least one linear ring." }
        require(coordinates.all { it.size >= 4 }) { "All linear rings must have at least four or more positions." }
        require(coordinates.all { it[0] == it.last() }) { "First and last position of a linear ring must match." }
    }

    val exteriorRing: MutableList<Position> get() = coordinates[0]
    val interiorRings: MutableList<MutableList<Position>>?
        get() = if (coordinates.size == 1) null else coordinates.subList(
            1,
            coordinates.size
        )
}

@Serializable
@SerialName("MultiPolygon")
class MultiPolygon(override val coordinates: MutableList<MutableList<MutableList<Position>>>) :
    Geometry() {
    init {
        require(coordinates.isNotEmpty()) { "MultiPolygon must contain at least one Polygon" }
        require(coordinates.all { it.isNotEmpty() }) { "MultiPolygon must contain at least one linear ring." }
        require(coordinates.all { it.all { it.size >= 4 } }) { "All linear rings must have at least four or more positions." }
        require(coordinates.all { it.all { it[0] == it.last() } }) { "First and last position of a linear ring must match." }
    }

    val polygons: List<Polygon> get() = coordinates.map { Polygon(it) }
}

@Serializable
@SerialName("GeometryCollection")
class GeometryCollection(
    val geometries: MutableList<Geometry>
) : AbstractGeometry() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GeometryCollection

        if (geometries != other.geometries) return false

        return true
    }

    override fun hashCode(): Int {
        return geometries.hashCode()
    }
}

class PolygonBuilder {

    private lateinit var exteriorRing: List<Position>
    private val interiorRings: MutableList<List<Position>> = mutableListOf()

    private fun validatePolygon() {
        if (exteriorRing.size < 4) throw RuntimeException(
            "Invalid exterior ring for polygon. " +
                    "Exterior ring: $exteriorRing"
        )
    }

    fun exteriorRing(vararg positions: Position): PolygonBuilder {
        this.exteriorRing = positions.toMutableList()
        return this
    }

    fun addHole(vararg positions: Position): PolygonBuilder {
        interiorRings.add(positions.toMutableList())
        return this
    }

    fun build(): Polygon {
        validatePolygon()

        val polygonCoordinates = mutableListOf(exteriorRing.toMutableList())
        if (interiorRings.isNotEmpty()) {
            polygonCoordinates.addAll(interiorRings.map { it.toMutableList() })
        }
        return Polygon(polygonCoordinates)
    }
}