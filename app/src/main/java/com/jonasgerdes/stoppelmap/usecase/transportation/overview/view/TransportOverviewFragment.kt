package com.jonasgerdes.stoppelmap.usecase.transportation.overview.view

import android.arch.lifecycle.LifecycleFragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.TransportationRepository
import com.jonasgerdes.stoppelmap.usecase.map.view.search.RouteAdapter
import com.jonasgerdes.stoppelmap.usecase.transportation.route_detail.view.RouteDetailActivity
import com.jonasgerdes.stoppelmap.usecase.transportation.station_detail.view.StationDetailActivity
import com.jonasgerdes.stoppelmap.util.plusAssign
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.transportation_overview_fragment.*
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
class TransportOverviewFragment : LifecycleFragment() {

    //todo: refactor in presenter and interactor, there is no time right now...

    private val disposables = CompositeDisposable()

    @Inject lateinit var repository: TransportationRepository

    init {
        App.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.transportation_overview_fragment, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        routes.adapter = RouteAdapter()

        with(routes.adapter as RouteAdapter) {
            disposables += repository.getRoutes().subscribe {
                routes = it
            }
            disposables += getSelections().subscribe {
                when (it.type) {
                    RouteAdapter.RouteSelection.TYPE_STATIONS
                    -> startActivity(RouteDetailActivity.createIntent(context, it.route.uuid!!))

                    RouteAdapter.RouteSelection.TYPE_RETURN
                    -> startActivity(Intent(context, StationDetailActivity::class.java))
                }
            }
        }
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}