package com.jonasgerdes.stoppelmap.preparation.util

import mobi.waterdog.kgeojson.Point
import mobi.waterdog.kgeojson.Position


fun List<Position>.center(): Position {
    val min = min()
    val max = max()
    return Position(lat = (min.lat + max.lat) / 2, lng = (min.lng + max.lng) / 2)
}


fun List<Position>.min() = Position(minBy { it.lng }.lng, minBy { it.lat }.lat)
fun List<Position>.max() = Position(maxBy { it.lng }.lng, maxBy { it.lat }.lat)

val Point.position get() = Position(lng = coordinates[0], lat = coordinates[1])