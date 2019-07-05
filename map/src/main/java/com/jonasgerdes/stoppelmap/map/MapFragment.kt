package com.jonasgerdes.stoppelmap.map

import android.os.Bundle
import android.view.View
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : BaseFragment<Route.Map>(R.layout.fragment_map) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        motionLayout.setOnApplyWindowInsetsListener { v, insets ->
            listOf(
                R.id.idle,
                R.id.search
            ).forEach {
                motionLayout.getConstraintSet(it).setGuidelineBegin(R.id.guideTop, insets.systemWindowInsetTop)
            }
            motionLayout.updateState()
            insets
        }
        motionLayout.requestApplyInsets()


        searchInput.onBackPress {
            Router.navigateBack()
        }

        searchCard.setOnClickListener {
            Router.navigateToRoute(Route.Map(state = Route.Map.State.Search()), Router.Destination.MAP)
        }


    }

    override fun processRoute(route: Route.Map) {
        when (route.state) {
            is Route.Map.State.Idle -> {
                motionLayout.transitionToState(R.id.idle)
                searchInput.clearFocus()
            }
            is Route.Map.State.Search -> {
                motionLayout.transitionToState(R.id.search)
                searchInput.requestFocus()
                searchInput.showKeyboard()

            }
        }
    }
}