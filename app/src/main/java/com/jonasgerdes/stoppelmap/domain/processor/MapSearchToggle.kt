package com.jonasgerdes.stoppelmap.domain.processor

import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import io.reactivex.Observable

class MapSearchToggle
    : BaseOperation<MapSearchToggle.Action>(Action::class.java) {

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.map { Result(it.showSearch) }

    data class Action(val showSearch: Boolean) : BaseAction

    data class Result(val showSearch: Boolean) : BaseResult
}