package com.jonasgerdes.stoppelmap.domain.processor

import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.model.map.search.HighlightedText
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MapSearch
    : BaseOperation<MapSearch.Action>(Action::class.java) {

    val database: StoppelMapDatabase by inject()
    var lastQuery = ""

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.subscribeOn(Schedulers.io())
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

                    searchStallsByName(query, queryParts)
                            .map(this::createResult)
                            .startWith(Result.Pending())
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