package com.jonasgerdes.stoppelmap.util

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.17
 */

fun <T> ArrayList<T>.addIfNotNull(value: T?) {
    if (value != null) {
        add(value)
    }
}