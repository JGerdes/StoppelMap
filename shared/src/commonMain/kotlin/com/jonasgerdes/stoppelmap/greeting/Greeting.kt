package com.jonasgerdes.stoppelmap.greeting

import getPlatform

class Greeting(
    private val greet: String,
) {
    private val platform = getPlatform()

    fun greet(): String {
        return "$greet, Viel Spaẞ mit deinem ${platform.name}!"
    }
}