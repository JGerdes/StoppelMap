package com.jonasgerdes.stoppelmap.preperation

import java.text.SimpleDateFormat
import java.util.*

object Util {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
}

fun currentTime() = Util.format.format(Calendar.getInstance().time)!!


fun <T> List<T>?.emptyIfNull(): List<T> {
    return this ?: emptyList()
}