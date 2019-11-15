package com.jonasgerdes.stoppelmap.transport.di

import androidx.lifecycle.ViewModel
import com.jonasgerdes.stoppelmap.core.di.ViewModelClass
import com.jonasgerdes.stoppelmap.transport.view.TransportViewModel
import com.jonasgerdes.stoppelmap.transport.view.route.RouteDetailViewModel
import com.jonasgerdes.stoppelmap.transport.view.station.StationDetailViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class TransportModule {

    @Provides
    @IntoMap
    @ViewModelClass(TransportViewModel::class)
    fun transportViewModel(viewModel: TransportViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelClass(StationDetailViewModel::class)
    fun stationViewModel(viewModel: StationDetailViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelClass(RouteDetailViewModel::class)
    fun routeViewModel(viewModel: RouteDetailViewModel): ViewModel = viewModel
}