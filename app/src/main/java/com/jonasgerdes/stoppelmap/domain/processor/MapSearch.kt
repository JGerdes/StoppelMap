package com.jonasgerdes.stoppelmap.domain.processor

import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MapSearch
    : BaseOperation<MapSearch.Action>(Action::class.java) {

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.map { it.copy(query = it.query.trim()) }
            .switchMap {
                if (it.query.isEmpty()) {
                    Observable.just(Result.EmptyQuery())
                } else {
                    Observable.just(emptyList<SearchResult>())
                            .subscribeOn(Schedulers.io())
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