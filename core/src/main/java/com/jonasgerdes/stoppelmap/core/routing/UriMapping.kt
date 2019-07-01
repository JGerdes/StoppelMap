package com.jonasgerdes.stoppelmap.core.routing

import android.net.Uri

private const val SEGMENT_NEWS = "news"
private const val SEGMENT_MAP = "map"
private const val SEGMENT_SCHEDULE = "schedule"
private const val SEGMENT_TRANSPORT = "transport"

fun createRouteFromUri(destination: Uri): Route? {
    return when {
        destination.pathSegments.isEmpty() -> Route.Home()
        destination.pathSegments.first() == SEGMENT_MAP -> Route.Map()
        destination.pathSegments.first() == SEGMENT_SCHEDULE -> Route.Schedule()
        destination.pathSegments.first() == SEGMENT_TRANSPORT -> Route.Transport()
        destination.pathSegments.first() == SEGMENT_NEWS -> Route.News()
        else -> null
    }
}