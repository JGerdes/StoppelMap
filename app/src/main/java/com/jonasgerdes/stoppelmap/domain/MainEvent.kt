package com.jonasgerdes.stoppelmap.domain

import com.jonasgerdes.mvi.BaseEvent

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.05.2018
 */
sealed class MainEvent : BaseEvent {
    class InitialEvent : MainEvent()
    sealed class MapEvent : MainEvent() {
        class SearchFieldClickedEvent : MapEvent()
        class OnBackPressEvent : MapEvent()
        data class MapItemClickedEvent(val slug: String) : MapEvent()
        object MapClickedEvent: MapEvent()
        data class StallCardSelected(val cardIndex: Int) : MapEvent()
        class MapMoved() : MapEvent()
        data class SearchResultClicked(val searchResultId: String) : MapEvent()
        data class QueryEntered(val query: String) : MapEvent()
    }

    sealed class FeedEvent : MainEvent() {
        class ReloadTriggered : FeedEvent()
        data class ItemClicked(val url: String) : FeedEvent()
    }
}