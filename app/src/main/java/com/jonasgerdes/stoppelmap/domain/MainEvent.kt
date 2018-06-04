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
        class MapItemClickedEvent(val slug: String) : MapEvent()
    }
    sealed class FeedEvent: MainEvent() {
        class ReloadTriggered : FeedEvent()
    }
}