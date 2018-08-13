package com.jonasgerdes.stoppelmap.bus.station

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
        fun start(context: Context, stationSlug: String) =
                context.startActivity(Intent(context, StationActivity::class.java).apply {
                    putExtra(EXTRA_STATION_SLUG, stationSlug)
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bus_station_activity)

        back.setOnClickListener {
            onBackPressed()
        }

        departures.adapter = adapter
        departures.itemAnimator = null
        (departures.layoutManager as GridLayoutManager).spanSizeLookup =
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) = adapter.items[position].getSpanSize()
                }

        viewModel.setStation(intent.getStringExtra(EXTRA_STATION_SLUG))

        viewModel.state.observeWith(this) {
            adapter.submitList(it.departureItems)
            stationTitle.text = it.station.name
            prices.text = it.prices.joinToString("\n") {
                val price = "%.2f€".format(it.price / 100.0)
                "${it.type}: $price"
            }
        }
    }
}
