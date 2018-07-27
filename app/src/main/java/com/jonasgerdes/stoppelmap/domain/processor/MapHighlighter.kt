package com.jonasgerdes.stoppelmap.domain.processor

import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.map.MapHighlight
import com.jonasgerdes.stoppelmap.map.highlightArea
import com.jonasgerdes.stoppelmap.model.map.*
import com.jonasgerdes.stoppelmap.model.map.entity.SubType
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult
import com.mapbox.mapboxsdk.geometry.LatLng
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MapHighlighter
    : BaseOperation<MapHighlighter.Action>(Action::class.java) {

    companion object {
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
        data class Highlight(val highlight: MapHighlight) : Result()
        data class HighlightStallsWithCards(val cards: List<StallCard>, val highlight: MapHighlight) : Result()
        object NoHighlight : Result()
    }

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.flatMap {
        when (it) {
            is Action.SelectNothing -> Observable.just(Result.NoHighlight)
            is Action.StallSelect -> Observable.just(highlightSingleStall(it))
            is Action.ResultSelect -> highlightResult(it)
            is MapHighlighter.Action.HighlightCard -> Observable.just(
                    Result.Highlight(highlightCard(it.cardIndex))
            )
        }
    }

    fun highlightCard(cardIndex: Int) =
            inMemoryDatabase.getStallCard(cardIndex)?.let {
                when (it) {
                    is SingleStallCard -> it.stall.highlightArea()
                    is StallCollectionCard -> if (it.stalls.size == 1) {
                        it.stalls.first().highlightArea()
                    } else {
                        MapHighlight.MultiplePoints(
                                it.stalls.map {
                                    MarkerItem(LatLng(it.centerLat, it.centerLng),
                                            it.type.type)
                                },
                                it.title
                        )
                    }
                }
            } ?: MapHighlight.None

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
                        SingleStallCard(
                                stall = stall,
                                images = images.filter { it.stall == stall.slug },
                                items = database.items().getByStall(stall.slug),
                                subTypes = database.subTypes().getByStall(stall.slug),
                                type = database.subTypes().getType(stall.type.type).first()
                        )
                    }.let {
                        if (stallsWithouthName.isNotEmpty()) {
                            it + listOf(StallCollectionCard(title!!, type, subType,
                                    stallsWithouthName, stallsWithName.isNotEmpty()))
                        } else {
                            it
                        }
                    }
                }
                .subscribeOn(Schedulers.io())
                .toObservable()
                .doOnNext { inMemoryDatabase.setStallCards(it) }
                .map { Result.HighlightStallsWithCards(it, highlightCard(0)) }

    }

    private fun highlightSingleStall(action: MapHighlighter.Action.StallSelect): Result {
        return database.stalls().getBySlug(action.slug)
                ?.let {
                    val images = database.images().getAllForStall(it.slug)
                    val items = database.items().getByStall(it.slug)
                    val subTypes = database.subTypes().getByStall(it.slug)
                    val cards = listOf(SingleStallCard(
                            stall = it,
                            images = images,
                            items = items,
                            subTypes = subTypes,
                            type = database.subTypes().getType(it.type.type).firstOrNull()
                                    ?: SubType(it.type.type, it.type.type))
                    )
                    inMemoryDatabase.setStallCards(cards)
                    Result.HighlightStallsWithCards(cards, highlightCard(0))
                }
                ?: Result.NoHighlight
    }
}