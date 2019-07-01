package com.jonasgerdes.stoppelmap.core.routing

sealed class Route {
    class Home : Route()
    class Map : Route()
    class Schedule : Route()
    class Transport : Route()
    data class News(val forceReload:Boolean = false, val scrollToTop: Boolean = false) : Route()
}