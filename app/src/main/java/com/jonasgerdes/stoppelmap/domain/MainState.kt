package com.jonasgerdes.stoppelmap.domain

import com.jonasgerdes.stoppelmap.map.MapHighlight
import com.jonasgerdes.stoppelmap.model.news.FeedItemWithImages
import com.jonasgerdes.stoppelmap.model.news.NewsItem

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.05.2018
 */
data class MainState(
        val map: MapState,
        val feed: FeedState
) {
    data class MapState(
            val searchExtended: Boolean,
            val highlight: MapHighlight
    )

    data class FeedState(
            val newsItems: List<FeedItemWithImages>,
            val isLoading: Boolean,
            val errorMessage: String?
    )
}