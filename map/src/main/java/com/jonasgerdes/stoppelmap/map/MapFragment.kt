package com.jonasgerdes.stoppelmap.map

import android.os.Bundle
import android.view.View
import com.jonasgerdes.stoppelmap.core.routing.Route
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

        mapView.isFocusable = true
        mapView.isFocusableInTouchMode = true
        searchInput.isFocusable = true
        searchInput.isFocusableInTouchMode = true
        searchInput.clearFocus()

        searchInput.setOnFocusChangeListener { view, focus ->
            if (focus) {
                motionLayout.transitionToState(R.id.search)
            } else {
                motionLayout.transitionToState(R.id.idle)
            }
        }

        searchInput.onBackPress {
            searchInput.clearFocus()
        }

    }
}