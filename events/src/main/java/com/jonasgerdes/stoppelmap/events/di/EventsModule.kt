package com.jonasgerdes.stoppelmap.events.di

import androidx.lifecycle.ViewModel
import com.jonasgerdes.stoppelmap.core.di.ViewModelClass
import com.jonasgerdes.stoppelmap.events.view.EventsViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class EventsModule {

    @Provides
    @IntoMap
    @ViewModelClass(EventsViewModel::class)
    fun eventsViewModel(viewModel: EventsViewModel): ViewModel = viewModel
}