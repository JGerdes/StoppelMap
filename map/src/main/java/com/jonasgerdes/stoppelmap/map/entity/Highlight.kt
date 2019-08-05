package com.jonasgerdes.stoppelmap.map.entity

import com.jonasgerdes.stoppelmap.model.map.Item
import com.jonasgerdes.stoppelmap.model.map.Stall
import com.jonasgerdes.stoppelmap.model.map.SubType

sealed class Highlight {
    data class SingleStall(val stall: FullStall) : Highlight()
    data class TypeCollection(val type: SubType, val stalls: List<FullStall>) : Highlight()
    data class ItemCollection(val item: Item, val stalls: List<FullStall>) : Highlight()
    data class NamelessStall(val stall: FullStall) : Highlight()
}

fun Highlight.getStalls() = when (this) {
    is Highlight.SingleStall -> listOf(stall)
    is Highlight.TypeCollection -> stalls
    is Highlight.ItemCollection -> stalls
    is Highlight.NamelessStall -> listOf(stall)
}