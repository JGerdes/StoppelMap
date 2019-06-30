package com.jonasgerdes.stoppelmap.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.jonasgerdes.androidutil.navigation.FragmentFactory
import com.jonasgerdes.androidutil.navigation.Navigator
import com.jonasgerdes.androidutil.navigation.createFragmentNavigator
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.core.routing.*
import com.jonasgerdes.stoppelmap.core.util.enableTransparentStatusBar
import com.jonasgerdes.stoppelmap.core.widget.BaseActivity
import kotlinx.android.synthetic.main.activity_stoppelmap.*
import org.koin.android.ext.android.inject

class StoppelMapActivity : BaseActivity(R.layout.activity_stoppelmap), Router.Navigator {

    private val fragmentFactory: FragmentFactory<Route> by inject()
    private val navigator by lazy {
        createFragmentNavigator(
            R.id.fragmentHost,
            supportFragmentManager,
            fragmentFactory,
            mapOf(
                Router.Destination.HOME to Route.Home(),
                Router.Destination.MAP to Route.Map(),
                Router.Destination.SCHEDULE to Route.Schedule(),
                Router.Destination.TRANSPORT to Route.Transport(),
                Router.Destination.NEWS to Route.News()
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)


        navigation.itemIconTintList = null
        window.enableTransparentStatusBar()

        Router.navigator = this

        val journeyFromAction = Action.fromString(intent.action)?.toRoute()
        when {
            savedInstanceState != null -> navigator.onLoadState(savedInstanceState)
            journeyFromAction != null -> {
                Router.navigateToRoute(journeyFromAction.route, journeyFromAction.destination, clearBackStack = true)
            }
            intent.action == Intent.ACTION_VIEW && intent.data != null -> resolveUri(intent.data)
            else -> Router.switchToDestination(Router.Destination.HOME)
        }


        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> navigator.switchToDestination(Router.Destination.HOME)
                R.id.nav_map -> navigator.switchToDestination(Router.Destination.MAP)
                R.id.nav_schedule -> navigator.switchToDestination(Router.Destination.SCHEDULE)
                R.id.nav_transport -> navigator.switchToDestination(Router.Destination.TRANSPORT)
                R.id.nav_news -> navigator.switchToDestination(Router.Destination.NEWS)
            }
            true
        }
    }

    private fun resolveUri(destination: Uri) {
        val resolvedJourney = createJourneyFromUri(destination) ?: Route.Home() to Router.Destination.HOME
        Router.navigateToRoute(resolvedJourney.route, resolvedJourney.destination, clearBackStack = true)
    }

    private fun updateNavigation(destination: Router.Destination) {
        val itemId = when (destination) {
            Router.Destination.HOME -> R.id.nav_home
            Router.Destination.MAP -> R.id.nav_map
            Router.Destination.SCHEDULE -> R.id.nav_schedule
            Router.Destination.TRANSPORT -> R.id.nav_transport
            Router.Destination.NEWS -> R.id.nav_news
        }
        if (itemId != navigation.selectedItemId) {
            navigation.selectedItemId = itemId
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navigator.onSaveInstanceState(outState)

    }

    override fun navigateToRoute(route: Route, destination: Router.Destination, clearBackStack: Boolean) {
        navigator.navigateToRoute(route, destination, clearBackStack)
        updateNavigation(destination)
    }

    override fun switchToDestination(destination: Router.Destination) {
        navigator.switchToDestination(destination)
        updateNavigation(destination)
    }

    override fun navigateBack(): Router.NavigateBackResult {
        return navigator.navigateBack().let { result ->
            when(result) {
                Navigator.NavigateBackResult.HANDLED -> Router.NavigateBackResult.HANDLED
                Navigator.NavigateBackResult.UNHANDLED -> Router.NavigateBackResult.UNHANDLED
            }
        }
    }

    override fun onBackPressed() {
        when (Router.navigateBack()) {
            Router.NavigateBackResult.UNHANDLED -> super.onBackPressed()
            Router.NavigateBackResult.HANDLED -> {
                //do nothing
            }
        }
    }
}
