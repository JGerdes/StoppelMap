package com.jonasgerdes.stoppelmap.settings.util

inline fun <reified T : Enum<T>> saveValueOf(value: String?, default: T): T =
    value?.let {
        try {
            enumValueOf<T>(it)
        } catch (iae: IllegalArgumentException) {
            default
        }
    } ?: default
