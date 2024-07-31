package com.jonasgerdes.stoppelmap.base.contract

fun interface PathFactory {
    fun create(file: String): String
}