package com.jonasgerdes.stoppelmap.domain

import com.jonasgerdes.mvi.BaseReducer
import com.jonasgerdes.mvi.BaseResult

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.05.2018
 */
class MainReducer : BaseReducer<MainState> {
    override fun idle() = MainState(
            MainState.MapState(
                    searchExtended = false
            )
    )

    override fun reduce(currentState: MainState, result: BaseResult) =
            when (result) {

                else -> currentState.copy()
            }
}