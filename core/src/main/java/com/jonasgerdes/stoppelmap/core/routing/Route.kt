package com.jonasgerdes.stoppelmap.core.routing

sealed class Route {
    class Home : Route()
    class Map : Route()
    class Schedule : Route()
    class Transport : Route()
    class News : Route()
}