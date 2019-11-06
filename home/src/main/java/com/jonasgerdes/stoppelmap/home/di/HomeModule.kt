package com.jonasgerdes.stoppelmap.home.di

import androidx.lifecycle.ViewModel
import com.jonasgerdes.stoppelmap.core.di.ViewModelClass
import com.jonasgerdes.stoppelmap.home.view.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.FlowPreview

@FlowPreview
@Module
abstract class HomeModule {
    @Binds
    @IntoMap
    @ViewModelClass(HomeViewModel::class)
    abstract fun homeViewModel(viewModel: HomeViewModel): ViewModel
}