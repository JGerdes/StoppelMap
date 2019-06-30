package com.jonasgerdes.androidutil

inline fun <reified T : Enum<T>> valueOfOrNull(name: String): T? {
    return try {
        enumValueOf<T>(name)
    } catch (e: IllegalArgumentException) {
        null
    }
}