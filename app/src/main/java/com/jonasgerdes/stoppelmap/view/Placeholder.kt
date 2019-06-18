package com.jonasgerdes.stoppelmap.view

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import kotlinx.android.synthetic.main.fragment_placeholder.*

abstract class PlaceholderFragment<R: Route>(layoutRes: Int, @StringRes val titleRes: Int) : BaseFragment<R>(layoutRes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title.setText(titleRes)
    }
}

class HomePlaceholderFragment : PlaceholderFragment<Route.Home>(
    R.layout.fragment_placeholder,
    R.string.main_bottom_nav_item_home
)

class MapPlaceholderFragment : PlaceholderFragment<Route.Map>(
    R.layout.fragment_placeholder,
    R.string.main_bottom_nav_item_map
)

class SchedulePlaceholderFragment : PlaceholderFragment<Route.Schedule>(
    R.layout.fragment_placeholder,
    R.string.main_bottom_nav_item_schedule
)

class TransportPlaceholderFragment : PlaceholderFragment<Route.Transport>(
    R.layout.fragment_placeholder,
    R.string.main_bottom_nav_item_transport
)

class NewsPlaceholderFragment : PlaceholderFragment<Route.News>(
    R.layout.fragment_placeholder,
    R.string.main_bottom_nav_item_news
)