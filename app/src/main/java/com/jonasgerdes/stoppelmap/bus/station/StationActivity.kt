package com.jonasgerdes.stoppelmap.bus.station

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.util.observeWith
import kotlinx.android.synthetic.main.bus_station_activity.*

class StationActivity : AppCompatActivity() {

    private val viewModel: StationViewModel by lazy {
        ViewModelProviders.of(this).get(StationViewModel::class.java)
    }


    val adapter = DepartureAdapter()

    companion object {
        private const val EXTRA_STATION_SLUG = "EXTRA_STATION_SLUG"
        private const val EXTRA_STATION_NAME = "EXTRA_STATION_NAME"
        fun start(context: Context, stationSlug: String, stationName: String?) =
                context.startActivity(Intent(context, StationActivity::class.java).apply {
                    putExtra(EXTRA_STATION_SLUG, stationSlug)
                    putExtra(EXTRA_STATION_NAME, stationName)
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bus_station_activity)

        departures.adapter = adapter
        departures.itemAnimator = null
        (departures.layoutManager as GridLayoutManager).spanSizeLookup =
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) = adapter.items[position].getSpanSize()
                }

        viewModel.setStation(intent.getStringExtra(EXTRA_STATION_SLUG))

        val font = ResourcesCompat.getFont(this, R.font.roboto_slab_light)
        toolbarLayout.setExpandedTitleTypeface(font)
        toolbarLayout.setCollapsedTitleTypeface(font)
        intent.getStringExtra(EXTRA_STATION_NAME)?.let {
            toolbar.title = it
        }

        viewModel.state.observeWith(this) {
            adapter.submitList(it.departureItems)
        }
    }
}
