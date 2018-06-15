package com.jonasgerdes.stoppelmap.domain

import com.jonasgerdes.mvi.BaseReducer
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.domain.processor.*
import com.jonasgerdes.stoppelmap.map.MapHighlight
import com.jonasgerdes.stoppelmap.map.highlightArea

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.05.2018
 */
class MainReducer : BaseReducer<MainState> {
    override fun idle() = MainState(
            MainState.MapState(
                    searchExtended = false,
                    highlight = MapHighlight.None,
                    cards = emptyList(),
                    results = emptyList(),
                    isPending = false,
                    showEmptyQueryMessage = false,
                    showNoResultMessage = false
            ),
            MainState.FeedState(
                    newsItems = emptyList(),
                    isLoading = false,
                    errorMessage = null
            )
    )

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun reduce(state: MainState, result: BaseResult): MainState {
        with(state) {
            return when (result) {
                is MapSearchToggle.Result -> copy(map = map.copy(searchExtended = result.showSearch))
                is MapHighlighter.Result.HighlightSingleStall
                -> copy(map = map.copy(highlight = result.stall.highlightArea(), searchExtended = false))
                is MapHighlighter.Result.HighlightStallsWithCards
                -> copy(map = map.copy(cards = result.cards))
                is MapHighlighter.Result.HighlightStallsCollection
                -> copy(map = map.copy(highlight = result.highlight))
                is MapHighlighter.Result.NoHighlight
                -> copy(map = map.copy(highlight = MapHighlight.None, cards = emptyList()))
                is MapSearch.Result.Pending
                -> copy(map = map.copy(
                        isPending = true,
                        showEmptyQueryMessage = false,
                        showNoResultMessage = false))
                is MapSearch.Result.EmptyQuery
                -> copy(map = map.copy(results = emptyList(),
                        isPending = false,
                        showEmptyQueryMessage = true,
                        showNoResultMessage = false))
                is MapSearch.Result.NoResults
                -> copy(map = map.copy(results = emptyList(),
                        isPending = false,
                        showEmptyQueryMessage = false,
                        showNoResultMessage = true))
                is MapSearch.Result.Success
                -> copy(map = map.copy(results = result.results,
                        isPending = false,
                        showEmptyQueryMessage = false,
                        showNoResultMessage = false))

                is FeedProvider.Result -> copy(feed = feed.copy(newsItems = result.news))
                is FeedItemLoader.Result.Pending -> copy(feed = feed.copy(isLoading = true, errorMessage = null))
                is FeedItemLoader.Result.Success -> copy(feed = feed.copy(isLoading = false, errorMessage = null))
                is FeedItemLoader.Result.NetworkError -> copy(feed = feed.copy(isLoading = false, errorMessage = "No Network"))

                else -> state.copy()
            }
        }
    }
}