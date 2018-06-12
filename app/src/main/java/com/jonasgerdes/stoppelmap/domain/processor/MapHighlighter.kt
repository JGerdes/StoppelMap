package com.jonasgerdes.stoppelmap.domain.processor

import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.model.map.InMemoryDatabase
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.model.map.entity.Stall
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult
import io.reactivex.Observable

class MapHighlighter
    : BaseOperation<MapHighlighter.Action>(Action::class.java) {

    private val database: StoppelMapDatabase by inject()
    private val inMemoryDatabase: InMemoryDatabase by inject()

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.map {
        when (it) {
            MapHighlighter.Action.SelectNothing -> Result.NoHighlight
            is Action.StallSelect -> database.stalls().getBySlug(it.slug)
                    ?.let { Result.HighlightSingleStall(it) }
                    ?: Result.NoHighlight
            is MapHighlighter.Action.ResultSelect -> {
                inMemoryDatabase.getSearchResultById(it.resultId)?.let {
                    when (it) {
                        is SearchResult.SingleStallResult ->
                            Result.HighlightStallsWithCards(listOf(it.stall))
                        is SearchResult.ItemResult ->
                            Result.HighlightStallsWithCards(it.stalls)
                    }
                } ?: Result.NoHighlight
            }
        }
    }

    sealed class Action : BaseAction {
        data class StallSelect(val slug: String) : Action()
        data class ResultSelect(val resultId: String) : Action()
        object SelectNothing : Action()
    }

    sealed class Result : BaseResult {
        data class HighlightSingleStall(val stall: Stall) : Result()
        data class HighlightStallsWithCards(val stalls: List<Stall>) : Result()
        object NoHighlight : Result()
    }
}