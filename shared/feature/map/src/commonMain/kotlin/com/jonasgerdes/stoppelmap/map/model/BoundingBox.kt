package com.jonasgerdes.stoppelmap.map.model

data class BoundingBox(
    val southLat: Double,
    val westLng: Double,
    val northLat: Double,
    val eastLng: Double,
)

fun List<BoundingBox>.reduce() = BoundingBox(
    southLat = minBy { it.southLat }.southLat,
    westLng = minBy { it.westLng }.westLng,
    northLat = maxBy { it.northLat }.northLat,
    eastLng = maxBy { it.eastLng }.eastLng
)

// TODO: crossinline accessor?
inline fun <T> Collection<T>.reduceBoundingBox(accessor: T.() -> BoundingBox) = BoundingBox(
    southLat = minBy { it.accessor().southLat }.accessor().southLat,
    westLng = minBy { it.accessor().westLng }.accessor().westLng,
    northLat = maxBy { it.accessor().northLat }.accessor().northLat,
    eastLng = maxBy { it.accessor().eastLng }.accessor().eastLng
)

fun BoundingBox.contains(location: Location): Boolean {
    if (location.lat < southLat) return false
    if (location.lng < westLng) return false
    if (location.lat > northLat) return false
    if (location.lng > eastLng) return false
    return true
}