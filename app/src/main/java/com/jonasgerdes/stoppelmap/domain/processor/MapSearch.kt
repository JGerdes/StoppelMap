package com.jonasgerdes.stoppelmap.domain.processor

import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.model.map.InMemoryDatabase
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.model.map.search.HighlightedText
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult
import com.jonasgerdes.stoppelmap.util.squared
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MapSearch
    : BaseOperation<MapSearch.Action>(Action::class.java) {

    val database: StoppelMapDatabase by inject()
    val inMemoryDatabase: InMemoryDatabase by inject()
    var lastQuery = ""

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.subscribeOn(Schedulers.io())
            .delay(100, TimeUnit.MILLISECONDS)
            .map {
                when (it) {
                    is Action.Search -> it.query.trim()
                    is Action.Refresh -> lastQuery
                }
            }
            .doOnNext { lastQuery = it }
            .switchMap {
                if (it.isEmpty()) {
                    Observable.just(Result.EmptyQuery())
                } else {
                    val queryParts = it.split(" ").map { it.trim() }
                    val query = queryParts.joinToString("%%").let { "%$it%" }

                    Observable.concat(
                            searchStallsByName(query, queryParts),
                            searchStallsByAlias(query, queryParts),
                            searchStallsByItemName(query, queryParts),
                            searchStallsBySubTypeName(query, queryParts)
                    ).flatMapIterable { it }
                            .toList()
                            .map { it.apply { sortByDescending(this@MapSearch::getMatchingFactor) } }
                            .toObservable()
                            .doOnNext { inMemoryDatabase.setSearchResults(it) }
                            .map(this::createResult)
                            .startWith(Result.Pending())
                }
            }

    private fun searchStallsByItemName(query: String, queryParts: List<String>):
            Observable<List<SearchResult.ItemResult>> {
        return database.items().searchByName(query)
                .map {
                    it.map { item ->
                        val title = HighlightedText.from(item.name, queryParts)
                        val stalls = database.stalls().getStallsByItemSlug(item.slug)
                        SearchResult.ItemResult(item, stalls, title)
                    }
                }.toObservable()
    }

    private fun searchStallsBySubTypeName(query: String, queryParts: List<String>):
            Observable<List<SearchResult.TypeResult>> {
        return database.subTypes().searchByName(query)
                .map {
                    it.map { item ->
                        val title = HighlightedText.from(item.name, queryParts)
                        val stalls = database.stalls().getStallsBySubType(item.slug).union(
                                database.stalls().getStallsByType(item.slug)
                        ).toList()
                        SearchResult.TypeResult(item, stalls, title)
                    }
                }.toObservable()
    }

    private fun getMatchingFactor(result: SearchResult): Int {
        return result.highlights.sumBy {
            (it.length * (if (it.start == 0) 5 else 1)).squared()
        }
    }

    private fun searchStallsByName(query: String, queryParts: List<String>):
            Observable<List<SearchResult.SingleStallResult>> {
        return database.stalls().searchByName(query)
                .map {
                    it.map {
                        SearchResult.SingleStallResult(it, HighlightedText.from(it.name!!, queryParts))
                    }
                }.toObservable()
    }

    private fun searchStallsByAlias(query: String, queryParts: List<String>):
            Observable<List<SearchResult.SingleStallResult>> {
        return database.stalls().searchByAlias(query)
                .map {
                    it.map {
                        SearchResult.SingleStallResult(it.stall,
                                HighlightedText.from(it.stall.name!!),
                                HighlightedText.from(it.alias, queryParts))
                    }
                }.toObservable()
    }

    private fun createResult(results: List<SearchResult>) = if (results.isEmpty()) {
        Result.NoResults()
    } else {
        Result.Success(results)
    }

    sealed class Action : BaseAction {
        data class Search(val query: String) : Action()
        class Refresh : Action()
    }

    sealed class Result : BaseResult {
        data class Success(val results: List<SearchResult>) : Result()
        class NoResults : Result()
        class EmptyQuery : Result()
        class Pending : Result()
    }
}