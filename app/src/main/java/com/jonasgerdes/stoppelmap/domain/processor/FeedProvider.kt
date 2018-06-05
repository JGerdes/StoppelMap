package com.jonasgerdes.stoppelmap.domain.processor

import android.util.Log
import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.model.news.DynamicDatabase
import com.jonasgerdes.stoppelmap.model.news.FeedItem
import com.jonasgerdes.stoppelmap.model.news.FeedItemWithImages
import com.jonasgerdes.stoppelmap.model.news.NewsItem
import io.reactivex.Observable

class FeedProvider
    : BaseOperation<FeedProvider.Action>(Action::class.java) {

    private val database: DynamicDatabase by inject()

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.switchMap {
        database.feedItemsWithImages()
                .getAll()
                .map { Result(it) }
                .toObservable()
    }

    class Action : BaseAction

    data class Result(val news: List<FeedItemWithImages>) : BaseResult
}