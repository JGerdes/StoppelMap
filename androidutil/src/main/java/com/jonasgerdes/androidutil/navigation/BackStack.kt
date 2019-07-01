package com.jonasgerdes.androidutil.navigation

import java.util.*

class BackStack<ScreenType: Enum<ScreenType>, RouteType>(initValues: Map<ScreenType, RouteType>? = null) {
    private val stackMap = mutableMapOf<ScreenType, Stack<RouteType>>()
    private var currentScreen: ScreenType? = null

    init {
        initValues?.entries?.forEach { initValue ->
            stackMap[initValue.key] = Stack<RouteType>().apply { push(initValue.value) }
        }
    }

    fun push(screen: ScreenType, route: RouteType) {
        if (!stackMap.containsKey(screen)) {
            stackMap[screen] = Stack()
        }
        stackMap[screen]?.push(route)
    }

    fun pop() = currentScreen?.let { screen ->
        stackMap[screen]?.let { stack ->
            if (stack.empty()) null
            else stack.pop()
        }
    }
}