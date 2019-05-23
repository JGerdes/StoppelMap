package com.jonasgerdes.androidutil.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

typealias FragmentFactory<T> = (T) -> Fragment
typealias EnumFactory<T> = (name: String) -> T

private const val KEY_CURRENT_SCREEN = "CURRENT_SCREEN"

internal inline fun <reified T : Enum<T>> enumValueByName(name: String) = try {
    enumValueOf<T>(name)
} catch (e: IllegalArgumentException) {
    null
}

inline fun <reified T : Enum<T>> createFragmentScreenNavigator(
    @IdRes hostViewId: Int,
    fragmentManager: FragmentManager,
    noinline fragmentFactory: FragmentFactory<T>
) =
    FragmentScreenNavigator<T>(fragmentFactory) { enumValueOf(it) }.also { navigator ->
        navigator.hostViewId = hostViewId
        navigator.fragmentManager = fragmentManager
    }


class FragmentScreenNavigator<T : Enum<T>>(
    private val fragmentFactory: FragmentFactory<T>,
    private val enumFactory: EnumFactory<T>
) {

    private var currentScreen: T? = null
    @IdRes var hostViewId: Int? = null
    var fragmentManager: FragmentManager? = null

    fun saveState(bundle: Bundle) {
        currentScreen?.let { bundle.putString(KEY_CURRENT_SCREEN, it.name) }
    }

    fun loadState(bundle: Bundle) {
        val name = bundle.getString(KEY_CURRENT_SCREEN)
        currentScreen = enumFactory(name)
    }

    fun showScreen(screen: T) = hostViewId?.let { hostId ->
        fragmentManager?.let { manager ->
            manager.beginTransaction().apply {
                val existingFragment = manager.findFragmentByTag(screen.name)
                val currentFragment = currentScreen?.let { manager.findFragmentByTag(it.name) }

                val fragment = existingFragment ?: fragmentFactory(screen).also {
                    add(hostId, it, screen.name)
                }
                currentFragment?.let { hide(it) }
                show(fragment)
                currentScreen = screen
            }.commitNowAllowingStateLoss()
        }
    }

}