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

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.map { it.copy(query = it.query.trim()) }
            .switchMap {
                if (it.query.isEmpty()) {
                    Observable.just(Result.EmptyQuery())
                } else {
                    val queryParts = it.query.split(" ").map { it.trim() }
                    val query = queryParts.joinToString("%%").let { "%$it%" }
                    database.stalls().searchByName(query).toObservable()
                            .subscribeOn(Schedulers.io())
                            .map {
                                it.map {
                                    SearchResult.SingleStallResult(it, HighlightedText.from(it.name!!, queryParts))
                                }
                            }
                            .map {
                                if (it.isEmpty()) {
                                    Result.NoResults()
                                } else {
                                    Result.Success(it)
                                }
                            }
                            .startWith(Result.Pending())
                }
            }

    data class Action(val query: String) : BaseAction

    sealed class Result : BaseResult {
        data class Success(val results: List<SearchResult>) : Result()
        class NoResults : Result()
        class EmptyQuery : Result()
        class Pending : Result()
    }
}