package com.jonasgerdes.stoppelmap.domain

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.05.2018
 */
data class MainState(
        val map: MapState
) {
    data class MapState(
            val searchExtended: Boolean
    )
}