package com.jonasgerdes.stoppelmap.view

import android.os.Bundle
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.util.enableTransparentStatusBar
import com.jonasgerdes.stoppelmap.core.widget.BaseActivity
import kotlinx.android.synthetic.main.activity_stoppelmap.*

private const val KEY_CURRENT_SCREEN = "CURRENT_SCREEN"

class StoppelMapActivity : BaseActivity(R.layout.activity_stoppelmap), Router.Navigator {

    private var currentScreen: Screen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigation.itemIconTintList = null
        window.enableTransparentStatusBar()

        Router.navigator = this

        if (savedInstanceState == null) {
            Router.navigateToRoute(Route.Home())
        } else {
            currentScreen = Screen.valueOf(savedInstanceState.getString(KEY_CURRENT_SCREEN))
        }

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> Router.navigateToRoute(Route.Home())
                R.id.nav_map -> Router.navigateToRoute(Route.Map())
                R.id.nav_schedule -> Router.navigateToRoute(Route.Schedule())
                R.id.nav_transport -> Router.navigateToRoute(Route.Transport())
                R.id.nav_news -> Router.navigateToRoute(Route.News())
            }
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentScreen?.let { outState.putString(KEY_CURRENT_SCREEN, it.name) }

    }

    private fun Screen.createFragment() = when (this) {
        Screen.Home -> HomePlaceholderFragment()
        Screen.Map -> MapPlaceholderFragment()
        Screen.Schedule -> SchedulePlaceholderFragment()
        Screen.Transport -> TransportPlaceholderFragment()
        Screen.News -> NewsPlaceholderFragment()
    }


    private fun Route.toScreen() = when (this) {
        is Route.Home -> Screen.Home
        is Route.Map -> Screen.Map
        is Route.Schedule -> Screen.Schedule
        is Route.Transport -> Screen.Transport
        is Route.News -> Screen.News
        else -> Screen.Home
    }

    override fun navigateToRoute(route: Route) {
        val screen = route.toScreen()
        supportFragmentManager.beginTransaction().apply {
            val existingFragment = supportFragmentManager.findFragmentByTag(screen.name)
            val currentFragment = currentScreen?.let { supportFragmentManager.findFragmentByTag(it.name) }

            val fragment = existingFragment ?: screen.createFragment().also {
                add(R.id.fragmentHost, it, screen.name)
            }
            currentFragment?.let { hide(it) }
            show(fragment)
            currentScreen = screen
        }.commitNowAllowingStateLoss()
    }
}
