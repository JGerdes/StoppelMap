package com.jonasgerdes.stoppelmap.domain.processor

import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.model.map.InMemoryDatabase
import com.jonasgerdes.stoppelmap.model.map.SingleStallCard
import com.jonasgerdes.stoppelmap.model.map.StallCard
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.model.map.entity.Stall
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult
import io.reactivex.Observable

class MapHighlighter
    : BaseOperation<MapHighlighter.Action>(Action::class.java) {

    private val database: StoppelMapDatabase by inject()
    private val inMemoryDatabase: InMemoryDatabase by inject()

    sealed class Action : BaseAction {
        data class StallSelect(val slug: String) : Action()
        data class ResultSelect(val resultId: String) : Action()
        data class HighlightCard(val cardIndex: Int) : Action()
        object SelectNothing : Action()
    }

    sealed class Result : BaseResult {
        data class HighlightSingleStall(val stall: Stall) : Result()
        data class HighlightStallsWithCards(val cards: List<StallCard>) : Result()
        object NoHighlight : Result()
    }

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.flatMap {
        when (it) {
            is Action.SelectNothing -> Observable.just(Result.NoHighlight)
            is Action.StallSelect -> Observable.just(highlightSingleStall(it))
            is Action.ResultSelect -> highlightResult(it)
            is MapHighlighter.Action.HighlightCard -> Observable.just(highlightCard(it))
        }
    }

    private fun highlightCard(action: Action.HighlightCard) =
            inMemoryDatabase.getStallCard(action.cardIndex)?.let {
                when (it) {
                    is SingleStallCard -> Result.HighlightSingleStall(it.stall)
                }
            } ?: Result.NoHighlight

    private fun highlightResult(action: Action.ResultSelect): Observable<Result> {
        val searchResult = inMemoryDatabase.getSearchResultById(action.resultId)
                ?: return Observable.just(Result.NoHighlight)
        val stalls = when (searchResult) {
            is SearchResult.SingleStallResult -> listOf(searchResult.stall)
            is SearchResult.ItemResult -> searchResult.stalls
            is SearchResult.TypeResult -> searchResult.stalls
        }
        return database.images().getAllForStalls(stalls.map { it.slug }.toTypedArray())
                .map { images ->
                    stalls.map { stall ->
                        SingleStallCard(stall, images.filter { it.stall == stall.slug })
                    }
                }
                .toObservable()
                .doOnNext { inMemoryDatabase.setStallCards(it) }
                .map { Result.HighlightStallsWithCards(it) }

    }

    private fun highlightSingleStall(action: MapHighlighter.Action.StallSelect): Result {
        return database.stalls().getBySlug(action.slug)
                ?.let { Result.HighlightSingleStall(it) }
                ?: Result.NoHighlight
    }
}