package com.jonasgerdes.androidutil.navigation

inline fun <reified T : Enum<T>> valueOfOrNull(name: String): T? {
    return try {
        enumValueOf<T>(name)
    } catch (e: IllegalArgumentException) {
        null
    }
}