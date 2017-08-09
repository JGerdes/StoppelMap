package com.jonasgerdes.stoppelmap.usecase.transportation.route_detail.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.TransportationRepository
import kotlinx.android.synthetic.main.transportation_route_detail_activity.*
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 10-08-17
 */

class RouteDetailActivity : AppCompatActivity() {

    //todo: refactor in presenter and interactor, there is no time right now...

    @Inject lateinit var repository: TransportationRepository

    init {
        App.graph.inject(this)
    }

    companion object {
        val EXTRA_ROUTE_SLUG = "EXTRA_ROUTE_SLUG"

        fun createIntent(context: Context, routeSlug: String): Intent {
            val intent = Intent(context, RouteDetailActivity::class.java)
            intent.putExtra(EXTRA_ROUTE_SLUG, routeSlug)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transportation_route_detail_activity)
        intent.extras?.getString(EXTRA_ROUTE_SLUG)?.let {
            repository.getRouteBy(it)?.let {
                stationList.adapter = StationAdapter()
                with(stationList.adapter as StationAdapter) {
                    stations = it.stations!!
                }
            }
        }

    }
}