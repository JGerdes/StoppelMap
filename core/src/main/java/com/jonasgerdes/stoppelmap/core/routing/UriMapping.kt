package com.jonasgerdes.stoppelmap.core.routing

import android.net.Uri

private const val SEGMENT_NEWS = "news"
private const val SEGMENT_MAP = "map"
private const val SEGMENT_SCHEDULE = "schedule"
private const val SEGMENT_TRANSPORT = "transport"

fun createJourneyFromUri(destination: Uri): Journey? {
    return when {
        destination.pathSegments.isEmpty() -> Route.Home() to Router.Destination.HOME
        destination.pathSegments.first() == SEGMENT_MAP -> Route.Map() to Router.Destination.MAP
        destination.pathSegments.first() == SEGMENT_SCHEDULE -> Route.Schedule() to Router.Destination.SCHEDULE
        destination.pathSegments.first() == SEGMENT_TRANSPORT -> Route.Transport() to Router.Destination.TRANSPORT
        destination.pathSegments.first() == SEGMENT_NEWS -> Route.News() to Router.Destination.NEWS
        else -> null
    }
}