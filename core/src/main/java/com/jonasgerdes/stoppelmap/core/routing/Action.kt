package com.jonasgerdes.stoppelmap.core.routing

enum class Action {
    OPEN_NEWS;

    companion object {

        private const val PREFIX = "com.jonasgerdes.stoppelmap.action."

        fun fromString(value: String): Action? {
            if (!value.startsWith(PREFIX)) {
                return null
            }
            return try {
                Action.valueOf(value.replaceFirst(PREFIX, ""))
            } catch (exception: IllegalArgumentException) {
                null
            }

        }
    }
}

fun Action?.toRoute() = when (this) {
    Action.OPEN_NEWS -> Route.News(scrollToTop = true)
    null -> null
}