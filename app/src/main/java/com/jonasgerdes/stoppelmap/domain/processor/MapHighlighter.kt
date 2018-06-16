package com.jonasgerdes.stoppelmap.domain.processor

import android.util.Log
import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.map.MapHighlight
import com.jonasgerdes.stoppelmap.model.map.*
import com.jonasgerdes.stoppelmap.model.map.entity.Stall
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult
import com.mapbox.mapboxsdk.geometry.LatLng
import io.reactivex.Observable

class MapHighlighter
    : BaseOperation<MapHighlighter.Action>(Action::class.java) {

    companion object {
        //filter to prevent stalls at the bus station to screw up zooming bounds
        const val maxIncludeLatitude = 52.746122
    }

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
        data class HighlightStallsCollection(val highlight: MapHighlight.MultiplePoints) : Result()
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
                    is StallCollectionCard -> if (it.stalls.size == 1) {
                        Result.HighlightSingleStall(it.stalls.first())
                    } else {
                        Result.HighlightStallsCollection(
                                MapHighlight.MultiplePoints(
                                        it.stalls.filter { it.centerLat > maxIncludeLatitude }
                                                .map { LatLng(it.centerLat, it.centerLng) },
                                        it.title
                                )
                        )
                    }
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

        val title = when (searchResult) {
            is SearchResult.SingleStallResult -> searchResult.stall.name
            is SearchResult.ItemResult -> searchResult.item.name
            is SearchResult.TypeResult -> searchResult.type.name
        }

        val type = when (searchResult) {
            is SearchResult.SingleStallResult -> searchResult.stall.type
            is SearchResult.ItemResult -> searchResult.stalls.first().type
            is SearchResult.TypeResult -> searchResult.stalls.first().type
        }
        val subType = (searchResult as? SearchResult.TypeResult)?.type?.slug

        val stallsWithName = stalls.filter { it.name != null }
        val stallsWithouthName = stalls.filter { it.name == null }
        return database.images().getAllForStalls(stalls.map { it.slug }.toTypedArray())
                .map { images ->
                    stallsWithName.map { stall ->
                        SingleStallCard(stall, images.filter { it.stall == stall.slug })
                    }.let {
                        if (stallsWithouthName.isNotEmpty()) {
                            it + listOf(StallCollectionCard(title!!, type, subType,
                                    stallsWithouthName, stallsWithName.isNotEmpty()))
                        } else {
                            it
                        }
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