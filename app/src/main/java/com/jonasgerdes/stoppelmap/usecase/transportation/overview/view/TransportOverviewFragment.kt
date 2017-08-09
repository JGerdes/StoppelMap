package com.jonasgerdes.stoppelmap.usecase.transportation.overview.view

import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.TransportationRepository
import com.jonasgerdes.stoppelmap.usecase.map.view.search.RouteAdapter
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

    @Inject lateinit var repository: TransportationRepository
    private val disposables = CompositeDisposable()

    init {
        App.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.transportation_overview_fragment, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RouteAdapter()
        routes.adapter = adapter
        disposables += repository.getRoutes().subscribe {
            adapter.routes = it
        }
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}