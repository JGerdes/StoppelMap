package com.jonasgerdes.stoppelmap.domain.processor

import android.util.Log
import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.model.news.DynamicDatabase
import com.jonasgerdes.stoppelmap.model.news.FeedImage
import com.jonasgerdes.stoppelmap.model.news.FeedItem
import com.jonasgerdes.stoppelmap.model.news.FeedItem.Companion.TYPE_NEWS
import com.jonasgerdes.stoppelmap.model.news.network.StoppelMapApi
import com.jonasgerdes.stoppelmap.util.toOffsetDateTime
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class FeedItemLoader
    : BaseOperation<FeedItemLoader.Action>(Action::class.java) {

    private val api: StoppelMapApi by inject()
    private val database: DynamicDatabase by inject()

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.flatMap {
        api.getNews()
                .subscribeOn(Schedulers.io())
                .doOnSuccess {
                    it.items.forEach { item ->
                        with(item) {
                            database.feedItems().insertItem(FeedItem(
                                    url = url,
                                    type = TYPE_NEWS,
                                    title = title,
                                    subTitle = subTitle,
                                    publishDate = publishDate.toOffsetDateTime(),
                                    content = content
                            ))
                        }
                        item.images?.map { FeedImage(it, item.url) }
                                ?.forEach { database.feedImages().insertItem(it) }
                    }
                }
                .toObservable()
                .map { Result.Success() as Result }
                .onErrorReturn {
                    Log.e("FeedItemLoader", "error during loading", it)
                    Result.NetworkError()
                }
                .startWith(Result.Pending())

    }

    class Action : BaseAction

    sealed class Result : BaseResult {
        class Pending : Result()
        class NetworkError : Result()
        class Success : Result()
    }
}