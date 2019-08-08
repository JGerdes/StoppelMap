package com.jonasgerdes.stoppelmap.map.entity

sealed class MapFocus {
    object None : MapFocus()
    data class All(val coordinates: List<Location>) : MapFocus()
}