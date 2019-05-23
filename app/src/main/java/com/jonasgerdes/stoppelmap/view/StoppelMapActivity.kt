package com.jonasgerdes.stoppelmap.view

import android.os.Bundle
import com.jonasgerdes.androidutil.navigation.createFragmentScreenNavigator
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.util.enableTransparentStatusBar
import com.jonasgerdes.stoppelmap.core.widget.BaseActivity
import kotlinx.android.synthetic.main.activity_stoppelmap.*


class StoppelMapActivity : BaseActivity(R.layout.activity_stoppelmap), Router.Navigator {

    private val fragmentNavigator by lazy {
        createFragmentScreenNavigator(
            R.id.fragmentHost,
            supportFragmentManager
        ) { screen: Screen -> createFragmentFor(screen) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigation.itemIconTintList = null
        window.enableTransparentStatusBar()

        Router.navigator = this

        if (savedInstanceState == null) {
            Router.navigateToRoute(Route.Home())
        } else {
            fragmentNavigator.loadState(savedInstanceState)
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
        fragmentNavigator.saveState(outState)

    }

    override fun navigateToRoute(route: Route) {
        val screen = route.toScreen()
        fragmentNavigator.showScreen(screen)
    }

    private fun createFragmentFor(screen: Screen) = when (screen) {
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
}
