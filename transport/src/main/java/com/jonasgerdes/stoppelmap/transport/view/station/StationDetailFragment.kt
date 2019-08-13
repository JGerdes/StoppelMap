package com.jonasgerdes.stoppelmap.transport.view.station

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.jonasgerdes.androidutil.view.consumeWindowInsetsTop
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.transport.R
import kotlinx.android.synthetic.main.fragment_option_list.toolbar
import kotlinx.android.synthetic.main.fragment_station_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class StationDetailFragment : Fragment(R.layout.fragment_station_detail) {

    val departureAdapter = DepartureAdapter()

    val title: String? by lazy { arguments!!.getString(ARGUMENT_TITLE) }
    val slug: String by lazy { arguments!!.getString(ARGUMENT_SLUG) }

    val viewModel: StationDetailViewModel by viewModel { parametersOf(slug) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.consumeWindowInsetsTop()
        if (title != null) toolbar.title = title
        toolbar.setNavigationOnClickListener {
            Router.navigateBack()
        }

        departureGrid.adapter = departureAdapter
        departureGrid.itemAnimator = null
        (departureGrid.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = departureAdapter.items[position].getSpanSize()
            }

        observe(viewModel.station) { station ->
            prices.text = station.prices.joinToString("\n") {
                val price = "%.2f€".format(it.price / 100.0)
                "${it.type}: $price"
            }

            departureAdapter.submitList(station.departures.flatMap { slot ->
                listOf(
                    DepartureGridItem.TimeSpanHeader(slot.type)
                ) + slot.slots
                    .filter { it.departures.isNotEmpty() }
                    .flatMap {
                        val maxSize = it.departures.map { it.departures.size }.sortedDescending().first()
                        (0 until maxSize).flatMap { i ->
                            it.departures.map {
                                it.departures.getOrNull(i)?.let { DepartureGridItem.Departure(it) }
                                    ?: DepartureGridItem.Empty
                            }
                        }
                    }
            })
        }
    }

    companion object {
        private const val ARGUMENT_SLUG = "argument_slug"
        private const val ARGUMENT_TITLE = "argument_title"
        fun newInstance(slug: String, title: String? = null) = StationDetailFragment().apply {
            arguments = bundleOf(
                ARGUMENT_SLUG to slug,
                ARGUMENT_TITLE to title
            )
        }
    }
}