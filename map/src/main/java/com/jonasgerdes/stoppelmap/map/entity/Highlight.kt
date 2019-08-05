package com.jonasgerdes.stoppelmap.map.entity

import com.jonasgerdes.stoppelmap.model.map.Stall

sealed class Highlight {
    data class SingleStall(val stall: Stall) : Highlight()
    data class Stalls(val stalls: List<Stall>) : Highlight()
}