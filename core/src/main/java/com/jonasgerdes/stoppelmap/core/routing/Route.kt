package com.jonasgerdes.stoppelmap.core.routing

sealed class Route {
    data class Home(val detail: HomeDetail? = null) : Route()
    class Map : Route()
    class Schedule : Route()
    class Transport : Route()
    data class News(val forceReload: Boolean = false, val scrollToTop: Boolean = false) : Route()
}

sealed class HomeDetail {
    class About : HomeDetail()
}