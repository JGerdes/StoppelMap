package com.jonasgerdes.stoppelmap.domain.processor

import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.domain.model.ExternalIntent
import io.reactivex.Observable

class FeedItemInteractor
    : BaseOperation<FeedItemInteractor.Action>(Action::class.java) {

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.flatMap {
        when (it) {
            is Action.OnFeedItemSelect -> Observable.just(
                    Result.OpenIntent(ExternalIntent.Url(it.itemUrl)),
                    Result.ResetIntent
            )
        }
    }

    sealed class Action : BaseAction {
        data class OnFeedItemSelect(val itemUrl: String) : Action()
    }

    sealed class Result : BaseResult {
        class OpenIntent(val intent: ExternalIntent) : Result()
        object ResetIntent : Result()
    }
}