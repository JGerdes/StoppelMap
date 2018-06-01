package com.jonasgerdes.stoppelmap.domain

import com.jonasgerdes.stoppelmap.domain.processor.MapHighlighter
import com.jonasgerdes.stoppelmap.map.MapHighlight
import com.jonasgerdes.stoppelmap.model.entity.Stall

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.05.2018
 */
data class MainState(
        val map: MapState
) {
    data class MapState(
            val searchExtended: Boolean,
            val highlight: MapHighlight
    )
}