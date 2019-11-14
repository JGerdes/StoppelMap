package com.jonasgerdes.stoppelmap.di

import androidx.lifecycle.ViewModelProvider
import com.jonasgerdes.stoppelmap.core.di.ViewModelModule
import com.jonasgerdes.stoppelmap.data.di.DataModule
import com.jonasgerdes.stoppelmap.events.di.EventsModule
import com.jonasgerdes.stoppelmap.home.di.HomeModule
import com.jonasgerdes.stoppelmap.map.di.MapModule
import com.jonasgerdes.stoppelmap.news.di.NewsModule
import dagger.Component
import javax.inject.Singleton


@Component(
    modules = [
        ContextModule::class,
        StoppelmapModule::class,
        ViewModelModule::class,
        DataModule::class,
        HomeModule::class,
        MapModule::class,
        EventsModule::class,
        NewsModule::class
    ]
)
@Singleton
interface AppComponent {
    fun viewModelFactory(): ViewModelProvider.Factory
}