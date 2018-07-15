package com.jonasgerdes.stoppelmap.domain

import com.jonasgerdes.stoppelmap.domain.model.ExternalIntent
import com.jonasgerdes.stoppelmap.domain.model.TransportRoute
import com.jonasgerdes.stoppelmap.map.MapHighlight
import com.jonasgerdes.stoppelmap.model.map.StallCard
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult
import com.jonasgerdes.stoppelmap.model.news.FeedItemWithImages
import com.jonasgerdes.stoppelmap.model.versioning.Message

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.05.2018
 */
data class MainState(
        val versionInfo: VersionInfo,
        val map: MapState,
        val transportation: TransportationState,
        val feed: FeedState
) {
    data class MapState(
            val searchExtended: Boolean,
            val highlight: MapHighlight,
            val cards: List<StallCard>,
            val results: List<SearchResult>,
            val showEmptyQueryMessage: Boolean,
            val showNoResultMessage: Boolean,
            val isPending: Boolean
    )

    data class TransportationState(
            val routes: List<TransportRoute>
    )

    data class FeedState(
            val newsItems: List<FeedItemWithImages>,
            val isLoading: Boolean,
            val errorMessage: String?,
            val externalIntent: ExternalIntent = ExternalIntent.None
    )

    data class VersionInfo(
            val messages: List<Message> = emptyList(),
            val newVersionAvailable: Boolean = false
    )
}