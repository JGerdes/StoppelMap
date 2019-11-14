package com.jonasgerdes.stoppelmap.map.di

import androidx.lifecycle.ViewModel
import com.jonasgerdes.stoppelmap.core.di.ViewModelClass
import com.jonasgerdes.stoppelmap.map.view.MapViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class MapModule {

    @Provides
    @IntoMap
    @ViewModelClass(MapViewModel::class)
    fun mapViewModel(viewModel: MapViewModel): ViewModel = viewModel
}