package com.jonasgerdes.stoppelmap.transport.entity

data class BusRoute(
    val title: String,
    val slug: String,
    val via: List<String>
)