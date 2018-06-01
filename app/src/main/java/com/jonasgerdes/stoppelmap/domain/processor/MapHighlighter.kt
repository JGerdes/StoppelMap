package com.jonasgerdes.stoppelmap.domain.processor

import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.model.entity.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.model.entity.Stall
import io.reactivex.Observable

class MapHighlighter
    : BaseOperation<MapHighlighter.Action>(Action::class.java) {

    val db by lazy { StoppelMapDatabase.database }

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.map {
        when (it) {
            is Action.StallSelect -> db.stalls().getBySlug(it.slug)
                    ?.let { Result.HighlightSingleStall(it) }
                    ?: Result.NoHighlight
        }
    }

    sealed class Action : BaseAction {
        data class StallSelect(val slug: String) : Action()
    }

    sealed class Result : BaseResult {
        data class HighlightSingleStall(val stall: Stall) : Result()
        object NoHighlight : Result()
    }
}