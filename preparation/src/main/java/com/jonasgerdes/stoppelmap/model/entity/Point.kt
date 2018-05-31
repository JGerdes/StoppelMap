package com.jonasgerdes.stoppelmap.model.entity

import com.github.filosganga.geogson.model.Coordinates
import com.github.filosganga.geogson.model.Polygon
import java.math.BigInteger

data class Point(val longitude: Double, val latitude: Double)

operator fun Point.div(count: Int): Point {
    return copy(longitude / count, latitude / count)
}


fun List<Coordinates>.center() = fold(Point(0.0, 0.0))
{ acc, pos ->
    acc.copy(acc.longitude + pos.lon,
            acc.latitude + pos.lat)
} / size


fun Polygon.coordinates() = positions().children().flatMap {
    it.children()
}.map { it.coordinates() }