package com.jonasgerdes.stoppelmap.bus.route

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.util.observeWith
import kotlinx.android.synthetic.main.bus_route_activity.*

class RouteActivity : AppCompatActivity() {

    private val viewModel: RouteViewModel by lazy {
        ViewModelProviders.of(this).get(RouteViewModel::class.java)
    }

    val adapter = StationAdapter()

    companion object {
        private const val EXTRA_ROUTE_SLUG = "EXTRA_ROUTE_SLUG"
        private const val EXTRA_ROUTE_NAME = "EXTRA_ROUTE_NAME"
        fun start(context: Context, routeSlug: String, routeName: String?) =
                context.startActivity(Intent(context, RouteActivity::class.java).apply {
                    putExtra(EXTRA_ROUTE_SLUG, routeSlug)
                    putExtra(EXTRA_ROUTE_NAME, routeName)
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bus_route_activity)

        stations.adapter = adapter
        stations.itemAnimator = null

        viewModel.setRoute(intent.getStringExtra(EXTRA_ROUTE_SLUG))

        val font = ResourcesCompat.getFont(this, R.font.roboto_slab_light)
        toolbarLayout.setExpandedTitleTypeface(font)
        toolbarLayout.setCollapsedTitleTypeface(font)
        intent.getStringExtra(EXTRA_ROUTE_NAME)?.let {
            toolbar.title = it
        }

        viewModel.state.observeWith(this) {
            adapter.submitList(it.stations + listOf(TransportStation.STOPPELMARKT))
        }
    }
}
