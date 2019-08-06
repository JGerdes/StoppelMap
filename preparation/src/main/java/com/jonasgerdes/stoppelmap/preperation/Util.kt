package com.jonasgerdes.stoppelmap.preperation

import com.google.common.reflect.TypeToken
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

object Util {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
}

fun currentTime() = Util.format.format(Calendar.getInstance().time)!!


fun <T> List<T>?.emptyIfNull(): List<T> {
    return this ?: emptyList()
}

inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

fun LocalDateTime.toOffsetAtStoppelmarkt() =
    atZone(TimeZone.getTimeZone("Europe/Berlin").toZoneId())
        .toOffsetDateTime()
