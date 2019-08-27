package com.jonasgerdes.stoppelmap.core.routing

import android.net.Uri
import android.util.Log

private const val SEGMENT_NEWS = "news"
private const val SEGMENT_MAP = "map"
private const val SEGMENT_SCHEDULE = "schedule"
private const val SEGMENT_TRANSPORT = "transport"

fun createJourneyFromUri(destination: Uri): Journey? {
    return when {
        destination.pathSegments.isEmpty() -> Route.Home() to Router.Destination.HOME
        destination.pathSegments.first() == SEGMENT_MAP -> Route.Map(state = Route.Map.State.Idle()) to Router.Destination.MAP
        destination.pathSegments.first() == SEGMENT_SCHEDULE -> Route.Schedule() to Router.Destination.SCHEDULE
        destination.pathSegments.first() == SEGMENT_TRANSPORT
        -> Route.Transport(state = Route.Transport.State.OptionsList()) to Router.Destination.TRANSPORT
        destination.pathSegments.first() == SEGMENT_NEWS -> Route.News() to Router.Destination.NEWS
        else -> createFromLegacyUri(destination)
    }
}

fun createFromLegacyUri(destination: Uri): Journey? {
    Log.d("UriMapping", "destination: $destination, ${destination.pathSegments}")
    val segments = destination.pathSegments
    if (segments.size == 3) {
        if (segments.first() == "2019" && segments[1] == "map") {
            return Route.Map(
                state = Route.Map.State.Carousel(
                    Route.Map.State.Carousel.StallCollection.Single(segments[2])
                )
            ) to Router.Destination.MAP
        }
    }
    return null
}
