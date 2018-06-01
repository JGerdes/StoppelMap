package com.jonasgerdes.stoppelmap.domain

import com.jonasgerdes.mvi.BaseReducer
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.domain.processor.MapHighlighter
import com.jonasgerdes.stoppelmap.domain.processor.MapSearchToggle
import com.jonasgerdes.stoppelmap.map.MapHighlight
import com.jonasgerdes.stoppelmap.map.highlight

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.05.2018
 */
class MainReducer : BaseReducer<MainState> {
    override fun idle() = MainState(
            MainState.MapState(
                    searchExtended = false,
                    highlight = MapHighlight.None
            )
    )

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun reduce(state: MainState, result: BaseResult): MainState {
        val map = state.map
        return when (result) {
            is MapSearchToggle.Result -> state.copy(map = map.copy(searchExtended = result.showSearch))
            is MapHighlighter.Result.HighlightSingleStall
            -> state.copy(map = map.copy(highlight = result.stall.highlight()))
            is MapHighlighter.Result.NoHighlight
            -> state.copy(map = map.copy(highlight = MapHighlight.None))

            else -> state.copy()
        }
    }
}