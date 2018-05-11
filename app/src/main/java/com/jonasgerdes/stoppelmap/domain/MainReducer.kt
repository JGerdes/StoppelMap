package com.jonasgerdes.stoppelmap.domain

import com.jonasgerdes.mvi.BaseReducer
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.domain.processor.MapSearchToggle

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

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun reduce(state: MainState, result: BaseResult): MainState {
        val map = state.map
        return when (result) {
            is MapSearchToggle.Result -> state.copy(map = map.copy(searchExtended = result.showSearch))
            else -> state.copy()
        }
    }
}