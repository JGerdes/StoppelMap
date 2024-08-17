package com.jonasgerdes.stoppelmap.schedule.model

sealed interface BookmarkedEvents {
    data object None : BookmarkedEvents
    data class Some(
        val past: List<Event>,
        val upcoming: List<Event>,
    ) : BookmarkedEvents
}