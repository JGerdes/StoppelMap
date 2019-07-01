package com.jonasgerdes.stoppelmap.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.jonasgerdes.androidutil.navigation.BackStack
import com.jonasgerdes.androidutil.navigation.FragmentScreenNavigator
import com.jonasgerdes.androidutil.navigation.createFragmentScreenNavigator
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.core.routing.*
import com.jonasgerdes.stoppelmap.core.util.enableTransparentStatusBar
import com.jonasgerdes.stoppelmap.core.widget.BaseActivity
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.core.widget.saveProcessRoute
import com.jonasgerdes.stoppelmap.news.view.NewsFragment
import kotlinx.android.synthetic.main.activity_stoppelmap.*


class StoppelMapActivity : BaseActivity(R.layout.activity_stoppelmap), Router.Navigator {

    private val backStack = BackStack(
        mapOf(
            Screen.Home to Route.Home(),
            Screen.Map to Route.Map(),
            Screen.Schedule to Route.Schedule(),
            Screen.Transport to Route.Transport(),
            Screen.News to Route.News()
        )
    )

    private val fragmentNavigator by lazy {
        createFragmentScreenNavigator(
            R.id.fragmentHost,
            supportFragmentManager
        ) { screen: Screen ->
            createFragmentFor(screen)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)


        navigation.itemIconTintList = null
        window.enableTransparentStatusBar()

        Router.navigator = this

        val routeFromAction = Action.fromString(intent.action)?.toRoute()
        when {
            routeFromAction != null -> {
                Router.navigateToRoute(routeFromAction)
                updateNavigation(routeFromAction)
            }
            intent.action == Intent.ACTION_VIEW && intent.data != null -> resolveUri(intent.data)
            savedInstanceState == null -> Router.navigateToRoute(Route.Home())
            else -> fragmentNavigator.loadState(savedInstanceState)
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

    private fun resolveUri(destination: Uri) {
        val resolvedRoute = createRouteFromUri(destination) ?: Route.Home()
        Router.navigateToRoute(resolvedRoute)
        updateNavigation(resolvedRoute)
    }

    private fun updateNavigation(route: Route) {
        navigation.selectedItemId = when (route) {
            is Route.Home -> R.id.nav_home
            is Route.Map -> R.id.nav_map
            is Route.Schedule -> R.id.nav_schedule
            is Route.Transport -> R.id.nav_transport
            is Route.News -> R.id.nav_news
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragmentNavigator.saveState(outState)

    }

    override fun navigateToRoute(route: Route) {
        val screen = route.toScreen()
        val result = fragmentNavigator.showScreen(screen)
        if (result is FragmentScreenNavigator.ShowScreenResult.Success) {
            val fragment = result.fragment
            if ((fragment is BaseFragment<*>)) {
                if (result.reselected) fragment.onReselected()
                fragment.saveProcessRoute(route)
            }
            backStack.push(screen, route)
        }
    }

    override fun navigateBack(): Router.BackNavigationResult {
        val route = backStack.pop()
        if (route != null) {
            val screen = route.toScreen()
            val result = fragmentNavigator.showScreen(screen)
            if (result is FragmentScreenNavigator.ShowScreenResult.Success) {
                val fragment = result.fragment
                if ((fragment is BaseFragment<*>)) {
                    fragment.saveProcessRoute(route)
                }
            }
            return Router.BackNavigationResult.HANDLED
        }
        return Router.BackNavigationResult.EMPTY_STACK
    }

    private fun createFragmentFor(screen: Screen) = when (screen) {
        Screen.Home -> HomePlaceholderFragment()
        Screen.Map -> MapPlaceholderFragment()
        Screen.Schedule -> SchedulePlaceholderFragment()
        Screen.Transport -> TransportPlaceholderFragment()
        Screen.News -> NewsFragment()
    }

    private fun Route.toScreen() = when (this) {
        is Route.Home -> Screen.Home
        is Route.Map -> Screen.Map
        is Route.Schedule -> Screen.Schedule
        is Route.Transport -> Screen.Transport
        is Route.News -> Screen.News
        else -> Screen.Home
    }

    override fun onBackPressed() {
        when (Router.navigateBack()) {
            Router.BackNavigationResult.EMPTY_STACK -> super.onBackPressed()
            Router.BackNavigationResult.HANDLED -> {
                //do nothing
            }
        }
    }
}
